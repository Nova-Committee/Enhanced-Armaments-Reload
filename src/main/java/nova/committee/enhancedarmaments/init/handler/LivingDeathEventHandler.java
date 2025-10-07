package nova.committee.enhancedarmaments.init.handler;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nova.committee.enhancedarmaments.common.config.EAConfig;
import nova.committee.enhancedarmaments.core.Ability;
import nova.committee.enhancedarmaments.core.Experience;
import nova.committee.enhancedarmaments.util.EAUtil;
import nova.committee.enhancedarmaments.util.NBTUtil;
import net.minecraft.world.InteractionHand;

/**
 * 使用有效武器杀死目标时更新武器信息。用于更新经验、等级、能力等
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LivingDeathEventHandler {
    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (event.getSource().getDirectEntity() instanceof Player player && !(event.getSource().getDirectEntity() instanceof FakePlayer)) {

            ItemStack stack;
            InteractionHand hand = LivingHurtEventHandler.bowHandMap.getOrDefault(player, player.getUsedItemHand());
            stack = player.getItemInHand(hand);

            if (!stack.isEmpty() && EAUtil.canEnhanceMelee(stack.getItem())) {
                CompoundTag nbt = NBTUtil.loadStackNBT(stack);
                if (nbt.contains("EA_ENABLED")) {
                    if (Ability.ETHEREAL.hasAbility(nbt)) {
                        int newDamage = Math.max(0, player.getInventory().getSelected().getDamageValue() - (Ability.ETHEREAL.getLevel(nbt) * 2));
                        player.getInventory().getSelected().setDamageValue(newDamage);
                    }
                    addBonusExperience(event, player, stack, nbt);
                    updateLevel(player, stack, nbt);
                    NBTUtil.saveStackNBT(stack, nbt);
                }
            } else if (!stack.isEmpty() && EAUtil.canEnhanceRanged(stack.getItem())) {
                CompoundTag nbt = NBTUtil.loadStackNBT(stack);

                if (nbt.contains("EA_ENABLED")) {
                    if (Ability.ETHEREAL.hasAbility(nbt)) {
                        int newDamage = Math.max(0, player.getInventory().getSelected().getDamageValue() - (Ability.ETHEREAL.getLevel(nbt) * 2 + 1));
                        player.getInventory().getSelected().setDamageValue(newDamage);
                    }
                    addBonusExperience(event, player, stack, nbt);
                    updateLevel(player, stack, nbt);
                }
            }
        } else if (event.getSource().getDirectEntity() instanceof Arrow) {

            if (event.getSource().getEntity() instanceof Player player && event.getSource().getEntity() != null) {
                ItemStack stack = player.getInventory().getSelected();

                if (!stack.isEmpty()) {
                    CompoundTag nbt = NBTUtil.loadStackNBT(stack);
                    addBonusExperience(event, player, stack, nbt);
                    updateLevel(player, stack, nbt);

                }
            }
        }
    }

    /**
     * 每次目标死亡时调用。根据目标的生命值增加额外经验。
     *
     * @param event
     * @param nbt
     */
    private static void addBonusExperience(LivingDeathEvent event, Player player, ItemStack stack, CompoundTag nbt) {
        if (Experience.getLevel(nbt) < EAConfig.maxLevel) {
            if (event.getEntity() != null) {
                LivingEntity target = event.getEntity();
                int bonusExperience = 0;

                if (target.getMaxHealth() < 10) bonusExperience = 3;
                else if (target.getMaxHealth() > 9 && target.getMaxHealth() < 20) bonusExperience = 6;
                else if (target.getMaxHealth() > 19 && target.getMaxHealth() < 50) bonusExperience = 15;
                else if (target.getMaxHealth() > 49 && target.getMaxHealth() < 100) bonusExperience = 50;
                else if (target.getMaxHealth() > 99) bonusExperience = 70;

                Experience.setExperience(nbt, Experience.getExperience(nbt) + bonusExperience);
                player.sendSystemMessage(Component.literal(stack.getDisplayName().getString() + ChatFormatting.GRAY + " " +
                        Component.translatable("enhancedarmaments.misc.exp.get").getString() + " " +
                        ChatFormatting.BLUE + "" + bonusExperience + ChatFormatting.GRAY + "!"));
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
