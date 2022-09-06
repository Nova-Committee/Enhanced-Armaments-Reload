package nova.committee.enhancedarmaments.init.handler;

import com.google.common.collect.Multimap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import nova.committee.enhancedarmaments.Static;
import nova.committee.enhancedarmaments.common.entity.FakePlayer;
import nova.committee.enhancedarmaments.core.Ability;
import nova.committee.enhancedarmaments.core.Experience;
import nova.committee.enhancedarmaments.core.Rarity;
import nova.committee.enhancedarmaments.init.callback.EntityEvents;
import nova.committee.enhancedarmaments.init.callback.PlayerEvents;
import nova.committee.enhancedarmaments.init.callback.ProjectileImpactCallback;
import nova.committee.enhancedarmaments.util.EAUtil;
import nova.committee.enhancedarmaments.util.NBTUtil;

import java.util.Collection;

public class LivingHurtEventHandler {
    //this needs to be a player capability or this will be really random in Multiplayer!
    public static InteractionHand bowfriendlyhand;

    public static void onArrowHit() {
        ProjectileImpactCallback.EVENT.register((proj, hit) -> {
            if (!(proj instanceof Arrow arrow)) return false;
            final var owner = arrow.getOwner();
            if (!(owner instanceof Player player)) return false;
            if (hit == null) bowfriendlyhand = player.getUsedItemHand();
            return true;
        });

    }

    public static void onArrowShoot() {
        PlayerEvents.ON_ARROW_LOOSE.register((player, bow, world, charge, hasAmmo) -> {
            bowfriendlyhand = player.getUsedItemHand();
            return charge;
        });

    }

    public static void onHurt() {
        EntityEvents.ON_LIVING_DAMAGE_CALC.register((world, entity, damageSource, damageAmount) -> {

            if (damageSource.getDirectEntity() instanceof Player player && !(damageSource.getDirectEntity() instanceof FakePlayer))
            //PLAYER IS ATTACKER
            {
                LivingEntity target = (LivingEntity) entity;
                ItemStack stack;
                if (bowfriendlyhand == null)
                    stack = player.getItemInHand(player.getUsedItemHand());
                else
                    stack = player.getItemInHand(bowfriendlyhand);

                if (stack != ItemStack.EMPTY && EAUtil.canEnhanceWeapon(stack.getItem())) {
                    CompoundTag nbt = NBTUtil.loadStackNBT(stack);
                    if (nbt.contains("EA_ENABLED")) {
                        updateExperience(nbt, damageAmount);
                        updateLevel(player, stack, nbt);
                        float damage1 =  useRarity(damageAmount, stack, nbt);
                        return useWeaponAbilities(damage1, player, target, nbt);
                    }
                }
            } else if (entity instanceof Player player) {//PLAYER IS GETTING HURT
                Entity target = damageSource.getEntity();

                for (ItemStack stack : player.getInventory().armor) {
                    if (stack != null) {
                        if (EAUtil.canEnhanceArmor(stack.getItem())) {
                            CompoundTag nbt = NBTUtil.loadStackNBT(stack);

                            if (nbt.contains("EA_ENABLED")) {
                                if (EAUtil.isDamageSourceAllowed(damageSource)) {
                                    if (damageAmount < (player.getMaxHealth() + player.getArmorValue()))
                                        updateExperience(nbt, damageAmount);
                                    else
                                        updateExperience(nbt, 1);
                                    updateLevel(player, stack, nbt);
                                }
                                float damage1 = useRarity(damageAmount, stack, nbt);
                                return useArmorAbilities(damage1, player, target, nbt);
                            }
                        }
                    }
                }
            }
            return damageAmount;

        });

    }

    /**
     * 每次目标受伤时调用。用于为造成伤害的武器增加经验
     *
     * @param nbt 数据
     */
    private static void updateExperience(CompoundTag nbt, float dealedDamage) {
        if (Experience.getLevel(nbt) < Static.configHandler.getConfig().getMaxLevel()) {
            Experience.setExperience(nbt, Experience.getExperience(nbt) + 1 + (int) dealedDamage / 4);
        }
    }

    /**
     * 每次目标受伤时调用。用于造成更多伤害或更少伤害。
     *
     * @param nbt 数据
     */
    private static float useRarity(float amount, ItemStack stack, CompoundTag nbt) {
        Rarity rarity = Rarity.getRarity(nbt);
        if (rarity != Rarity.DEFAULT)

            if (EAUtil.canEnhanceMelee(stack.getItem())) {
                Multimap<Attribute, AttributeModifier> map = stack.getItem().getAttributeModifiers(stack, EquipmentSlot.MAINHAND);
                Collection<AttributeModifier> damageCollection = map.get(Attributes.ATTACK_DAMAGE);
                AttributeModifier damageModifier = (AttributeModifier) damageCollection.toArray()[0];
                double damage = damageModifier.getAmount();
                return (float) (amount + damage * rarity.getEffect());
            } else if (EAUtil.canEnhanceRanged(stack.getItem())) {
                return (float) (amount + (amount * rarity.getEffect() / 3));
            } else if (EAUtil.canEnhanceArmor(stack.getItem()))
                return (float) (amount / (1.0F + (rarity.getEffect() / 5F)));

        return amount;
    }

    /**
     * 每次目标受伤时调用。用于使用武器可能拥有的能力。
     *
     * @param amount  伤害
     * @param player 玩家
     * @param target 目标
     * @param nbt    数据
     */
    private static float useWeaponAbilities(float amount, Player player, LivingEntity target, CompoundTag nbt) {
        if (target != null) {
            // active
            if (Ability.FIRE.hasAbility(nbt) && (int) (Math.random() * Static.configHandler.getConfig().getFirechance()) == 0) {
                double multiplier = (Ability.FIRE.getLevel(nbt) + Ability.FIRE.getLevel(nbt) * 4) / 4D;
                target.setSecondsOnFire((int) (multiplier));
            }

            if (Ability.FROST.hasAbility(nbt) && (int) (Math.random() * Static.configHandler.getConfig().getFrostchance()) == 0) {
                double multiplier = (Ability.FROST.getLevel(nbt) + Ability.FROST.getLevel(nbt) * 4) / 3D;
                target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, (int) (20 * multiplier), 10));
            }

            if (Ability.POISON.hasAbility(nbt) && (int) (Math.random() * Static.configHandler.getConfig().getPoisonchance()) == 0) {
                double multiplier = (Ability.POISON.getLevel(nbt) + Ability.POISON.getLevel(nbt) * 4) / 2D;
                target.addEffect(new MobEffectInstance(MobEffects.POISON, (int) (20 * multiplier), Ability.POISON.getLevel(nbt)));
            }

            if (Ability.INNATE.hasAbility(nbt) && (int) (Math.random() * Static.configHandler.getConfig().getInnatechance()) == 0) {
                double multiplier = (Ability.INNATE.getLevel(nbt) + Ability.INNATE.getLevel(nbt) * 4) / 3D;
                target.addEffect(new MobEffectInstance(MobEffects.WITHER, (int) (20 * multiplier), Ability.INNATE.getLevel(nbt)));
            }

            if (Ability.BOMBASTIC.hasAbility(nbt) && (int) (Math.random() * Static.configHandler.getConfig().getBombasticchance()) == 0) {
                double multiplierD = (Ability.BOMBASTIC.getLevel(nbt) + Ability.BOMBASTIC.getLevel(nbt) * 4) / 4D;
                float multiplier = (float) multiplierD;
                Level world = target.getLevel();

                if (!(target instanceof Animal)) {
                    world.explode(target, target.getX(), target.getY(), target.getZ(), multiplier, Explosion.BlockInteraction.NONE);
                }
            }

            if (Ability.CRITICAL_POINT.hasAbility(nbt) && (int) (Math.random() * Static.configHandler.getConfig().getCriticalpointchance()) == 0) {
                float multiplier = 0F;

                if (Ability.CRITICAL_POINT.getLevel(nbt) == 1) multiplier = 0.17F;
                else if (Ability.CRITICAL_POINT.getLevel(nbt) == 2) multiplier = 0.34F;
                else if (Ability.CRITICAL_POINT.getLevel(nbt) == 3) multiplier = 0.51F;

                float damage = target.getMaxHealth() * multiplier;
                return amount + damage;
            }

            // passive
            if (Ability.ILLUMINATION.hasAbility(nbt)) {
                target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, (20 * 5), Ability.ILLUMINATION.getLevel(nbt)));
            }

            if (Ability.BLOODTHIRST.hasAbility(nbt)) {
                float addition = amount * (Ability.BLOODTHIRST.getLevel(nbt) * 12) / 100;
                player.setHealth(player.getHealth() + addition);
            }
        }
        return amount;
    }

    private static float useArmorAbilities(float amount, Player player, Entity target, CompoundTag nbt) {
        if (target != null) {
            // active
            if (Ability.MOLTEN.hasAbility(nbt) && (int) (Math.random() * Static.configHandler.getConfig().getMoltenchance()) == 0 && target instanceof LivingEntity realTarget) {
                double multiplier = (Ability.MOLTEN.getLevel(nbt) + Ability.MOLTEN.getLevel(nbt) * 5) / 4D;
                realTarget.setSecondsOnFire((int) (multiplier));
            }

            if (Ability.FROZEN.hasAbility(nbt) && (int) (Math.random() * Static.configHandler.getConfig().getFrozenchance()) == 0 && target instanceof LivingEntity realTarget) {
                double multiplier = (Ability.FROZEN.getLevel(nbt) + Ability.FROZEN.getLevel(nbt) * 5) / 6D;
                realTarget.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, (int) (20 * multiplier), 10));
            }

            if (Ability.TOXIC.hasAbility(nbt) && (int) (Math.random() * Static.configHandler.getConfig().getToxicchance()) == 0 && target instanceof LivingEntity realTarget) {
                double multiplier = (Ability.TOXIC.getLevel(nbt) + Ability.TOXIC.getLevel(nbt) * 4) / 4D;
                realTarget.addEffect(new MobEffectInstance(MobEffects.POISON, (int) (20 * multiplier), Ability.TOXIC.getLevel(nbt)));
            }

            if (Ability.ADRENALINE.hasAbility(nbt) && (int) (Math.random() * Static.configHandler.getConfig().getAdrenalinechance()) == 0) {
                double multiplier = (Ability.ADRENALINE.getLevel(nbt) + Ability.ADRENALINE.getLevel(nbt) * 5) / 3D;
                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, (int) (20 * (multiplier)), Ability.ADRENALINE.getLevel(nbt)));
            }

            // passive
            if (Ability.BEASTIAL.hasAbility(nbt)) {
                if (player.getHealth() <= (player.getMaxHealth() * 0.2F))
                    player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 20 * 7, 0));
            }

            if (Ability.HARDENED.hasAbility(nbt) && (int) (Math.random() * Static.configHandler.getConfig().getHardenedchance()) == 0) {
                return 0;
            }
        }
        return amount;
    }

    /**
     * 每次目标受伤时调用。用于检查武器是否应该升级。
     *
     * @param player
     * @param stack
     * @param nbt
     */
    private static void updateLevel(Player player, ItemStack stack, CompoundTag nbt) {
        int level = Experience.getNextLevel(player, stack, nbt, Experience.getLevel(nbt), Experience.getExperience(nbt));
        Experience.setLevel(nbt, level);
        NBTUtil.saveStackNBT(stack, nbt);
    }
}
