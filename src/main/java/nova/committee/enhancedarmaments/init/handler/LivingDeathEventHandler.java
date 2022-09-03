package nova.committee.enhancedarmaments.init.handler;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nova.committee.enhancedarmaments.common.config.Config;
import nova.committee.enhancedarmaments.core.Ability;
import nova.committee.enhancedarmaments.core.Experience;
import nova.committee.enhancedarmaments.util.EAUtil;
import nova.committee.enhancedarmaments.util.NBTUtil;

/**
 * 使用有效武器杀死目标时更新武器信息。用于更新经验、等级、能力等
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LivingDeathEventHandler {
    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (event.getSource().getDirectEntity() instanceof PlayerEntity && !(event.getSource().getDirectEntity() instanceof FakePlayer)) {
            PlayerEntity player = (PlayerEntity) event.getSource().getDirectEntity();

            ItemStack stack;
            if (LivingHurtEventHandler.bowfriendlyhand == null)
                stack = player.getItemInHand(player.getUsedItemHand());
            else
                stack = player.getItemInHand(LivingHurtEventHandler.bowfriendlyhand);

            if (stack != ItemStack.EMPTY && EAUtil.canEnhanceMelee(stack.getItem())) {
                CompoundNBT nbt = NBTUtil.loadStackNBT(stack);
                if (nbt.contains("EA_ENABLED")) {
                    if (Ability.ETHEREAL.hasAbility(nbt)) {
                        player.inventory.getSelected().setDamageValue((player.inventory.getSelected().getDamageValue() - (Ability.ETHEREAL.getLevel(nbt) * 2)));
                    }
                    addBonusExperience(event, nbt);
                    updateLevel(player, stack, nbt);
                    NBTUtil.saveStackNBT(stack, nbt);
                }
            } else if (stack != ItemStack.EMPTY && EAUtil.canEnhanceRanged(stack.getItem())) {
                CompoundNBT nbt = NBTUtil.loadStackNBT(stack);

                if (nbt.contains("EA_ENABLED")) {
                    if (Ability.ETHEREAL.hasAbility(nbt)) {
                        player.inventory.getSelected().setDamageValue((player.inventory.getSelected().getDamageValue() - (Ability.ETHEREAL.getLevel(nbt) * 2 + 1)));
                    }
                    addBonusExperience(event, nbt);
                    updateLevel(player, stack, nbt);
                }
            }
        } else if (event.getSource().getDirectEntity() instanceof ArrowEntity) {

            if (event.getSource().getEntity() instanceof PlayerEntity && event.getSource().getEntity() != null) {
                PlayerEntity player = (PlayerEntity) event.getSource().getEntity();
                if (player != null) {
                    ItemStack stack = player.inventory.getSelected();

                    if (stack != ItemStack.EMPTY) {
                        CompoundNBT nbt = NBTUtil.loadStackNBT(stack);
                        addBonusExperience(event, nbt);
                        updateLevel(player, stack, nbt);

                    }
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
    private static void addBonusExperience(LivingDeathEvent event, CompoundNBT nbt) {
        if (Experience.getLevel(nbt) < Config.maxLevel) {
            if (event.getEntityLiving() != null) {
                LivingEntity target = event.getEntityLiving();
                int bonusExperience = 0;

                if (target.getMaxHealth() < 10) bonusExperience = 3;
                else if (target.getMaxHealth() > 9 && target.getMaxHealth() < 20) bonusExperience = 6;
                else if (target.getMaxHealth() > 19 && target.getMaxHealth() < 50) bonusExperience = 15;
                else if (target.getMaxHealth() > 49 && target.getMaxHealth() < 100) bonusExperience = 50;
                else if (target.getMaxHealth() > 99) bonusExperience = 70;

                Experience.setExperience(nbt, Experience.getExperience(nbt) + bonusExperience);
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
    private static void updateLevel(PlayerEntity player, ItemStack stack, CompoundNBT nbt) {
        int level = Experience.getNextLevel(player, stack, nbt, Experience.getLevel(nbt), Experience.getExperience(nbt));
        Experience.setLevel(nbt, level);
    }
}
