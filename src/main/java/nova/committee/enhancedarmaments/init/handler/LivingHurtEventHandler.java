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
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nova.committee.enhancedarmaments.common.config.EAConfig;
import nova.committee.enhancedarmaments.core.Ability;
import nova.committee.enhancedarmaments.core.Experience;
import nova.committee.enhancedarmaments.core.Rarity;
import nova.committee.enhancedarmaments.util.EAUtil;
import nova.committee.enhancedarmaments.util.NBTUtil;

import java.util.Collection;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LivingHurtEventHandler {
    //this needs to be a player capability or this will be really random in Multiplayer!
    public static InteractionHand bowfriendlyhand;

    @SubscribeEvent
    public static void onArrowHit(ProjectileImpactEvent event) {
        if (!(event.getProjectile() instanceof Arrow)) return;
        if (!(event.getEntity() instanceof Player player)) return;
        if (event.getRayTraceResult() == null) bowfriendlyhand = player.getUsedItemHand();
    }

    @SubscribeEvent
    public static void onArrowShoot(ArrowLooseEvent event) {
        bowfriendlyhand = event.getEntity().getUsedItemHand();
    }

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        if (event.getSource().getDirectEntity() instanceof Player player && !(event.getSource().getDirectEntity() instanceof FakePlayer))
        //PLAYER IS ATTACKER
        {
            LivingEntity target = event.getEntity();
            ItemStack stack;
            if (bowfriendlyhand == null)
                stack = player.getItemInHand(player.getUsedItemHand());
            else
                stack = player.getItemInHand(bowfriendlyhand);

            if (stack != ItemStack.EMPTY && EAUtil.canEnhanceWeapon(stack.getItem())) {
                CompoundTag nbt = NBTUtil.loadStackNBT(stack);


                if (nbt.contains("EA_ENABLED")) {
                    updateExperience(nbt, event.getAmount());
                    useRarity(event, stack, nbt);
                    useWeaponAbilities(event, player, target, nbt);
                    updateLevel(player, stack, nbt);
                }
            }
        } else if (event.getEntity() instanceof Player player) {//PLAYER IS GETTING HURT
            Entity target = event.getSource().getEntity();

            for (ItemStack stack : player.getInventory().armor) {
                if (stack != null) {
                    if (EAUtil.canEnhanceArmor(stack.getItem())) {
                        CompoundTag nbt = NBTUtil.loadStackNBT(stack);

                        if (nbt.contains("EA_ENABLED")) {
                            if (EAUtil.isDamageSourceAllowed(event.getSource())) {
                                if (event.getAmount() < (player.getMaxHealth() + player.getArmorValue()))
                                    updateExperience(nbt, event.getAmount());
                                else
                                    updateExperience(nbt, 1);
                                updateLevel(player, stack, nbt);
                            }
                            useRarity(event, stack, nbt);
                            useArmorAbilities(event, player, target, nbt);
                        }
                    }
                }
            }
        }
    }

    /**
     * 每次目标受伤时调用。用于为造成伤害的武器增加经验
     *
     * @param nbt 数据
     */
    private static void updateExperience(CompoundTag nbt, float dealedDamage) {
        if (Experience.getLevel(nbt) < EAConfig.maxLevel) {
            Experience.setExperience(nbt, Experience.getExperience(nbt) + 1 + (int) dealedDamage / 4);
        }
    }

    /**
     * 每次目标受伤时调用。用于造成更多伤害或更少伤害。
     * 根据稀有度解决定造成的伤害
     *
     * @param nbt 数据
     */
    private static void useRarity(LivingHurtEvent event, ItemStack stack, CompoundTag nbt) {
        Rarity rarity = Rarity.getRarity(nbt);

        if (rarity != Rarity.DEFAULT)
            if (EAUtil.canEnhanceMelee(stack.getItem())) {
                Multimap<Attribute, AttributeModifier> map = stack.getItem().getAttributeModifiers(EquipmentSlot.MAINHAND, stack);
                Collection<AttributeModifier> damageCollection = map.get(Attributes.ATTACK_DAMAGE);
                AttributeModifier damageModifier = (AttributeModifier) damageCollection.toArray()[0];
                double damage = damageModifier.getAmount();
                event.setAmount((float) (event.getAmount() + damage * rarity.getEffect()));
            } else if (EAUtil.canEnhanceRanged(stack.getItem())) {
                float newdamage = (float) (event.getAmount() + (event.getAmount() * rarity.getEffect() / 3));
                event.setAmount(newdamage);
            } else if (EAUtil.canEnhanceArmor(stack.getItem()))
                event.setAmount((float) (event.getAmount() / (1.0F + (rarity.getEffect() / 5F))));
    }

    /**
     * 每次目标受伤时调用。用于使用武器可能拥有的能力。
     *
     * @param event  事件
     * @param player 玩家
     * @param target 目标
     * @param nbt    数据
     */
    private static void useWeaponAbilities(LivingHurtEvent event, Player player, LivingEntity target, CompoundTag nbt) {
        if (target != null) {
            // active
            if (Ability.FIRE.hasAbility(nbt) && (int) (Math.random() * EAConfig.firechance) == 0) {
                double multiplier = (Ability.FIRE.getLevel(nbt) + Ability.FIRE.getLevel(nbt) * 4) / 4.0;
                target.setSecondsOnFire((int) (multiplier));
            }

            if (Ability.FROST.hasAbility(nbt) && (int) (Math.random() * EAConfig.frostchance) == 0) {
                double multiplier = (Ability.FROST.getLevel(nbt) + Ability.FROST.getLevel(nbt) * 4) / 3.0;
                target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, (int) (20 * multiplier), 10));
            }

            if (Ability.POISON.hasAbility(nbt) && (int) (Math.random() * EAConfig.poisonchance) == 0) {
                double multiplier = (Ability.POISON.getLevel(nbt) + Ability.POISON.getLevel(nbt) * 4) / 2.0;
                target.addEffect(new MobEffectInstance(MobEffects.POISON, (int) (20 * multiplier), Ability.POISON.getLevel(nbt)));
            }

            if (Ability.INNATE.hasAbility(nbt) && (int) (Math.random() * EAConfig.innatechance) == 0) {
                double multiplier = (Ability.INNATE.getLevel(nbt) + Ability.INNATE.getLevel(nbt) * 4) / 3.0;
                target.addEffect(new MobEffectInstance(MobEffects.WITHER, (int) (20 * multiplier), Ability.INNATE.getLevel(nbt)));
            }

            if (Ability.BOMBASTIC.hasAbility(nbt) && (int) (Math.random() * EAConfig.bombasticchance) == 0) {
                double multiplierD = (Ability.BOMBASTIC.getLevel(nbt) + Ability.BOMBASTIC.getLevel(nbt) * 4) / 4.0;
                float multiplier = (float) multiplierD;
                Level world = target.getLevel();

                if (!(target instanceof Animal)) {
                    world.explode(target, target.getX(), target.getY(), target.getZ(), multiplier, Explosion.BlockInteraction.NONE);
                }
            }

            if (Ability.CRITICAL_POINT.hasAbility(nbt) && (int) (Math.random() * EAConfig.criticalpointchance) == 0) {
                float multiplier = 0F;

                if (Ability.CRITICAL_POINT.getLevel(nbt) == 1) multiplier = 0.17F;
                else if (Ability.CRITICAL_POINT.getLevel(nbt) == 2) multiplier = 0.34F;
                else if (Ability.CRITICAL_POINT.getLevel(nbt) == 3) multiplier = 0.51F;

                float damage = target.getMaxHealth() * multiplier;
                event.setAmount(event.getAmount() + damage);
            }

            // passive
            if (Ability.ILLUMINATION.hasAbility(nbt)) {
                target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, (20 * 5), Ability.ILLUMINATION.getLevel(nbt)));
            }

            if (Ability.BLOODTHIRST.hasAbility(nbt)) {
                float addition = event.getAmount() * (Ability.BLOODTHIRST.getLevel(nbt) * 12) / 100;
                player.setHealth(player.getHealth() + addition);
            }
        }
    }

    private static void useArmorAbilities(LivingHurtEvent event, Player player, Entity target, CompoundTag nbt) {
        if (target != null) {
            // active
            if (Ability.MOLTEN.hasAbility(nbt) && (int) (Math.random() * EAConfig.moltenchance) == 0 && target instanceof LivingEntity realTarget) {
                double multiplier = (Ability.MOLTEN.getLevel(nbt) + Ability.MOLTEN.getLevel(nbt) * 5) / 4.0;
                realTarget.setSecondsOnFire((int) (multiplier));
            }

            if (Ability.FROZEN.hasAbility(nbt) && (int) (Math.random() * EAConfig.frozenchance) == 0 && target instanceof LivingEntity realTarget) {
                double multiplier = (Ability.FROZEN.getLevel(nbt) + Ability.FROZEN.getLevel(nbt) * 5) / 6.0;
                realTarget.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, (int) (20 * multiplier), 10));
            }

            if (Ability.TOXIC.hasAbility(nbt) && (int) (Math.random() * EAConfig.toxicchance) == 0 && target instanceof LivingEntity realTarget) {
                double multiplier = (Ability.TOXIC.getLevel(nbt) + Ability.TOXIC.getLevel(nbt) * 4) / 4.0;
                realTarget.addEffect(new MobEffectInstance(MobEffects.POISON, (int) (20 * multiplier), Ability.TOXIC.getLevel(nbt)));
            }

            if (Ability.ADRENALINE.hasAbility(nbt) && (int) (Math.random() * EAConfig.adrenalinechance) == 0) {
                double multiplier = (Ability.ADRENALINE.getLevel(nbt) + Ability.ADRENALINE.getLevel(nbt) * 5) / 3.0;
                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, (int) (20 * (multiplier)), Ability.ADRENALINE.getLevel(nbt)));
            }

            // passive
            if (Ability.BEASTIAL.hasAbility(nbt)) {
                if (player.getHealth() <= (player.getMaxHealth() * 0.2F))
                    player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 20 * 7, 0));
            }

            if (Ability.HARDENED.hasAbility(nbt) && (int) (Math.random() * EAConfig.hardenedchance) == 0) {
                event.setAmount(0F);
            }
        }
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
