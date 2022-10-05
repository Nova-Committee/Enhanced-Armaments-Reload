package nova.committee.enhancedarmaments.init.handler;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import nova.committee.enhancedarmaments.Static;
import nova.committee.enhancedarmaments.common.entity.FakePlayer;
import nova.committee.enhancedarmaments.core.Ability;
import nova.committee.enhancedarmaments.core.Experience;
import nova.committee.enhancedarmaments.init.callback.EntityEvents;
import nova.committee.enhancedarmaments.util.EAUtil;
import nova.committee.enhancedarmaments.util.NBTUtil;

import java.util.UUID;

/**
 * 使用有效武器杀死目标时更新武器信息。用于更新经验、等级、能力等
 */
public class LivingDeathEventHandler {

    public static void init() {
        EntityEvents.LIVING_ENTITY_DEATH.register((world, entity, source) -> {
            if (source.getDirectEntity() instanceof Player player && !(source.getDirectEntity() instanceof FakePlayer)) {

                ItemStack stack;
                if (LivingHurtEventHandler.bowfriendlyhand == null)
                    stack = player.getItemInHand(player.getUsedItemHand());
                else
                    stack = player.getItemInHand(LivingHurtEventHandler.bowfriendlyhand);

                if (stack != ItemStack.EMPTY && EAUtil.canEnhanceMelee(stack.getItem())) {
                    CompoundTag nbt = NBTUtil.loadStackNBT(stack);
                    if (nbt.contains("EA_ENABLED")) {
                        if (Ability.ETHEREAL.hasAbility(nbt)) {
                            player.getInventory().getSelected().setDamageValue((player.getInventory().getSelected().getDamageValue() - (Ability.ETHEREAL.getLevel(nbt) * 2)));
                        }
                        addBonusExperience(player, stack, entity, nbt);
                        updateLevel(player, stack, nbt);
                        NBTUtil.saveStackNBT(stack, nbt);
                    }
                } else if (stack != ItemStack.EMPTY && EAUtil.canEnhanceRanged(stack.getItem())) {
                    CompoundTag nbt = NBTUtil.loadStackNBT(stack);

                    if (nbt.contains("EA_ENABLED")) {
                        if (Ability.ETHEREAL.hasAbility(nbt)) {
                            player.getInventory().getSelected().setDamageValue((player.getInventory().getSelected().getDamageValue() - (Ability.ETHEREAL.getLevel(nbt) * 2 + 1)));
                        }
                        addBonusExperience(player, stack, entity, nbt);
                        updateLevel(player, stack, nbt);
                    }
                }
            } else if (source.getDirectEntity() instanceof Arrow) {

                if (source.getEntity() instanceof Player player && source.getEntity() != null) {
                    ItemStack stack = player.getInventory().getSelected();

                    if (stack != ItemStack.EMPTY) {
                        CompoundTag nbt = NBTUtil.loadStackNBT(stack);
                        addBonusExperience(player, stack, entity, nbt);
                        updateLevel(player, stack, nbt);

                    }
                }
            }
        });

    }

    /**
     * 每次目标死亡时调用。根据目标的生命值增加额外经验。
     *
     */
    private static void addBonusExperience(Player player, ItemStack stack, Entity target, CompoundTag nbt) {
        if (Experience.getLevel(nbt) < Static.configHandler.getConfig().getMaxLevel()) {
            if (target != null) {
                int bonusExperience = 0;
                LivingEntity entity = (LivingEntity) target;

                if (entity.getMaxHealth() < 10) bonusExperience = 3;
                else if (entity.getMaxHealth() > 9 && entity.getMaxHealth() < 20) bonusExperience = 6;
                else if (entity.getMaxHealth() > 19 && entity.getMaxHealth() < 50) bonusExperience = 15;
                else if (entity.getMaxHealth() > 49 && entity.getMaxHealth() < 100) bonusExperience = 50;
                else if (entity.getMaxHealth() > 99) bonusExperience = 70;

                Experience.setExperience(nbt, Experience.getExperience(nbt) + bonusExperience);
                player.sendMessage(new TextComponent(stack.getDisplayName().getString() + ChatFormatting.GRAY + " " +
                        new TranslatableComponent("enhancedarmaments.misc.exp.get").getString() + " " +
                        ChatFormatting.BLUE + "" + bonusExperience + ChatFormatting.GRAY + "!"), Util.NIL_UUID);

            }
        }
    }

    /**
     * 每次有目标时调用。用于更新武器的等级。
     *
     * @param player
     * @param stack
     * @param nbt
     */
    private static void updateLevel(Player player, ItemStack stack, CompoundTag nbt) {
        int level = Experience.getNextLevel(player, stack, nbt, Experience.getLevel(nbt), Experience.getExperience(nbt));
        Experience.setLevel(nbt, level);
    }
}
