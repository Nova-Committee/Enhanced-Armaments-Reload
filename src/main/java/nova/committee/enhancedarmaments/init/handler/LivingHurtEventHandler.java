package nova.committee.enhancedarmaments.init.handler;

import com.google.common.collect.Multimap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Hand;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nova.committee.enhancedarmaments.common.config.Config;
import nova.committee.enhancedarmaments.core.Ability;
import nova.committee.enhancedarmaments.core.Experience;
import nova.committee.enhancedarmaments.core.Rarity;
import nova.committee.enhancedarmaments.util.EAUtil;
import nova.committee.enhancedarmaments.util.NBTUtil;

import java.util.Collection;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LivingHurtEventHandler {
    public static Hand bowfriendlyhand;

    @SubscribeEvent
    public static void onArrowHit(ProjectileImpactEvent.Arrow event) {
        if (event.getArrow() instanceof ArrowEntity) {
            if (event.getEntity() instanceof PlayerEntity && event.getEntity() != null) {
                PlayerEntity player = (PlayerEntity) event.getEntity();
                if (event.getRayTraceResult() == null && player != null)
                    bowfriendlyhand = player.getUsedItemHand();
            }
        }
    }

    @SubscribeEvent
    public static void onArrowShoot(ArrowLooseEvent event) {
        bowfriendlyhand = event.getPlayer().getUsedItemHand();
    }

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        if (event.getSource().getDirectEntity() instanceof PlayerEntity && !(event.getSource().getDirectEntity() instanceof FakePlayer))
        //PLAYER IS ATTACKER
        {
            PlayerEntity player = (PlayerEntity) event.getSource().getDirectEntity();
            LivingEntity target = event.getEntityLiving();
            ItemStack stack;
            if (bowfriendlyhand == null)
                stack = player.getItemInHand(player.getUsedItemHand());
            else
                stack = player.getItemInHand(bowfriendlyhand);

            if (stack != ItemStack.EMPTY && EAUtil.canEnhanceWeapon(stack.getItem())) {
                CompoundNBT nbt = NBTUtil.loadStackNBT(stack);


                if (nbt.contains("EA_ENABLED")) {
                    updateExperience(nbt, event.getAmount());
                    useRarity(event, stack, nbt);
                    useWeaponAbilities(event, player, target, nbt);
                    updateLevel(player, stack, nbt);
                }
            }
        } else if (event.getEntityLiving() instanceof PlayerEntity) {//PLAYER IS GETTING HURT
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            Entity target = event.getSource().getEntity();

            for (ItemStack stack : player.inventory.armor) {
                if (stack != null) {
                    if (EAUtil.canEnhanceArmor(stack.getItem())) {
                        CompoundNBT nbt = NBTUtil.loadStackNBT(stack);

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
    private static void updateExperience(CompoundNBT nbt, float dealedDamage) {
        if (Experience.getLevel(nbt) < Config.maxLevel) {
            Experience.setExperience(nbt, Experience.getExperience(nbt) + 1 + (int) dealedDamage / 4);
        }
    }

    /**
     * 每次目标受伤时调用。用于造成更多伤害或更少伤害。
     *
     * @param nbt 数据
     */
    private static void useRarity(LivingHurtEvent event, ItemStack stack, CompoundNBT nbt) {
        Rarity rarity = Rarity.getRarity(nbt);

        if (rarity != Rarity.DEFAULT)
            if (EAUtil.canEnhanceMelee(stack.getItem())) {
                Multimap<Attribute, AttributeModifier> map = stack.getItem().getAttributeModifiers(EquipmentSlotType.MAINHAND, stack);
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
    private static void useWeaponAbilities(LivingHurtEvent event, PlayerEntity player, LivingEntity target, CompoundNBT nbt) {
        if (target != null) {
            // active
            if (Ability.FIRE.hasAbility(nbt) && (int) (Math.random() * Config.firechance) == 0) {
                double multiplier = (Ability.FIRE.getLevel(nbt) + Ability.FIRE.getLevel(nbt) * 4) / 4.0;
                target.setSecondsOnFire((int) (multiplier));
            }

            if (Ability.FROST.hasAbility(nbt) && (int) (Math.random() * Config.frostchance) == 0) {
                double multiplier = (Ability.FROST.getLevel(nbt) + Ability.FROST.getLevel(nbt) * 4) / 3.0;
                target.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, (int) (20 * multiplier), 10));
            }

            if (Ability.POISON.hasAbility(nbt) && (int) (Math.random() * Config.poisonchance) == 0) {
                double multiplier = (Ability.POISON.getLevel(nbt) + Ability.POISON.getLevel(nbt) * 4) / 2.0;
                target.addEffect(new EffectInstance(Effects.POISON, (int) (20 * multiplier), Ability.POISON.getLevel(nbt)));
            }

            if (Ability.INNATE.hasAbility(nbt) && (int) (Math.random() * Config.innatechance) == 0) {
                double multiplier = (Ability.INNATE.getLevel(nbt) + Ability.INNATE.getLevel(nbt) * 4) / 3.0;
                target.addEffect(new EffectInstance(Effects.WITHER, (int) (20 * multiplier), Ability.INNATE.getLevel(nbt)));
            }

            if (Ability.BOMBASTIC.hasAbility(nbt) && (int) (Math.random() * Config.bombasticchance) == 0) {
                double multiplierD = (Ability.BOMBASTIC.getLevel(nbt) + Ability.BOMBASTIC.getLevel(nbt) * 4) / 4.0;
                float multiplier = (float) multiplierD;
                World world = target.getCommandSenderWorld();

                if (!(target instanceof AnimalEntity)) {
                    world.explode(target, target.getX(), target.getY(), target.getZ(), multiplier, Explosion.Mode.NONE);
                }
            }

            if (Ability.CRITICAL_POINT.hasAbility(nbt) && (int) (Math.random() * Config.criticalpointchance) == 0) {
                float multiplier = 0F;

                if (Ability.CRITICAL_POINT.getLevel(nbt) == 1) multiplier = 0.17F;
                else if (Ability.CRITICAL_POINT.getLevel(nbt) == 2) multiplier = 0.34F;
                else if (Ability.CRITICAL_POINT.getLevel(nbt) == 3) multiplier = 0.51F;

                float damage = target.getMaxHealth() * multiplier;
                event.setAmount(event.getAmount() + damage);
            }

            // passive
            if (Ability.ILLUMINATION.hasAbility(nbt)) {
                target.addEffect(new EffectInstance(Effects.WEAKNESS, (20 * 5), Ability.ILLUMINATION.getLevel(nbt)));
            }

            if (Ability.BLOODTHIRST.hasAbility(nbt)) {
                float addition = event.getAmount() * (Ability.BLOODTHIRST.getLevel(nbt) * 12) / 100;
                player.setHealth(player.getHealth() + addition);
            }
        }
    }

    private static void useArmorAbilities(LivingHurtEvent event, PlayerEntity player, Entity target, CompoundNBT nbt) {
        if (target != null) {
            // active
            if (Ability.MOLTEN.hasAbility(nbt) && (int) (Math.random() * Config.moltenchance) == 0 && target instanceof LivingEntity) {
                LivingEntity realTarget = (LivingEntity) target;
                double multiplier = (Ability.MOLTEN.getLevel(nbt) + Ability.MOLTEN.getLevel(nbt) * 5) / 4.0;
                realTarget.setSecondsOnFire((int) (multiplier));
            }

            if (Ability.FROZEN.hasAbility(nbt) && (int) (Math.random() * Config.frozenchance) == 0 && target instanceof LivingEntity) {
                LivingEntity realTarget = (LivingEntity) target;
                double multiplier = (Ability.FROZEN.getLevel(nbt) + Ability.FROZEN.getLevel(nbt) * 5) / 6.0;
                realTarget.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, (int) (20 * multiplier), 10));
            }

            if (Ability.TOXIC.hasAbility(nbt) && (int) (Math.random() * Config.toxicchance) == 0 && target instanceof LivingEntity) {
                LivingEntity realTarget = (LivingEntity) target;
                double multiplier = (Ability.TOXIC.getLevel(nbt) + Ability.TOXIC.getLevel(nbt) * 4) / 4.0;
                realTarget.addEffect(new EffectInstance(Effects.POISON, (int) (20 * multiplier), Ability.TOXIC.getLevel(nbt)));
            }

            if (Ability.ADRENALINE.hasAbility(nbt) && (int) (Math.random() * Config.adrenalinechance) == 0) {
                double multiplier = (Ability.ADRENALINE.getLevel(nbt) + Ability.ADRENALINE.getLevel(nbt) * 5) / 3.0;
                player.addEffect(new EffectInstance(Effects.REGENERATION, (int) (20 * (multiplier)), Ability.ADRENALINE.getLevel(nbt)));
            }

            // passive
            if (Ability.BEASTIAL.hasAbility(nbt)) {
                if (player.getHealth() <= (player.getMaxHealth() * 0.2F))
                    player.addEffect(new EffectInstance(Effects.DAMAGE_BOOST, 20 * 7, 0));
            }

            if (Ability.HARDENED.hasAbility(nbt) && (int) (Math.random() * Config.hardenedchance) == 0) {
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
    private static void updateLevel(PlayerEntity player, ItemStack stack, CompoundNBT nbt) {
        int level = Experience.getNextLevel(player, stack, nbt, Experience.getLevel(nbt), Experience.getExperience(nbt));
        Experience.setLevel(nbt, level);
        NBTUtil.saveStackNBT(stack, nbt);
    }
}