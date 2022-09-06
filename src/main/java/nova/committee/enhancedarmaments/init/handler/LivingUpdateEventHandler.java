package nova.committee.enhancedarmaments.init.handler;

import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import nova.committee.enhancedarmaments.Static;
import nova.committee.enhancedarmaments.core.Ability;
import nova.committee.enhancedarmaments.core.Experience;
import nova.committee.enhancedarmaments.core.Rarity;
import nova.committee.enhancedarmaments.init.callback.EntityEvents;
import nova.committee.enhancedarmaments.util.EAUtil;
import nova.committee.enhancedarmaments.util.NBTUtil;

import java.util.Objects;

public class LivingUpdateEventHandler {
    private static int count = 0;


    public static void onUpdate() {
        EntityEvents.LIVING_TICK.register((world, entity) ->{
            if (entity instanceof Player player) {
                NonNullList<ItemStack> main = player.getInventory().items;

                if (!player.level.isClientSide) {
                    for (ItemStack stack : player.getInventory().armor) {
                        if (stack != null && EAUtil.canEnhanceArmor(stack.getItem())) {
                            CompoundTag nbt = NBTUtil.loadStackNBT(stack);
                            float heal = Ability.REMEDIAL.getLevel(nbt);
                            if (Ability.REMEDIAL.hasAbility(nbt))
                                if (count < 120) {
                                    count++;
                                } else {
                                    count = 0;
                                    player.heal(heal);
                                }
                        }
                    }
                    for (ItemStack stack : main) {
                        if (stack != ItemStack.EMPTY) {
                            Item item = stack.getItem();

                            if (EAUtil.canEnhance(item)) {
                                CompoundTag nbt = NBTUtil.loadStackNBT(stack);
                                if (!Experience.isEnabled(nbt)) {
                                    boolean okay = true;

                                    for (int j = 0; j < Static.configHandler.getConfig().getItemBlacklist().size(); j++) {
                                        if (Objects.equals(Registry.ITEM.getKey(Static.configHandler.getConfig().getItemBlacklist().get(j)), Registry.ITEM.getKey(stack.getItem())))
                                            okay = false;
                                    }

                                    if (Static.configHandler.getConfig().getItemWhitelist().size() != 0) {
                                        okay = false;
                                        for (int k = 0; k < Static.configHandler.getConfig().getItemWhitelist().size(); k++)
                                            if (Objects.equals(Registry.ITEM.getKey(Static.configHandler.getConfig().getItemWhitelist().get(k)), Registry.ITEM.getKey(stack.getItem())))
                                                okay = true;
                                    }

                                    if (okay) {
                                        Experience.enable(nbt, true);
                                        Rarity rarity = Rarity.getRarity(nbt);
                                        RandomSource rand = player.level.random;

                                        if (rarity == Rarity.DEFAULT) {
                                            rarity = Rarity.getRandomRarity(rand);
                                            rarity.setRarity(nbt);
                                            NBTUtil.saveStackNBT(stack, nbt);
                                        }
                                    }
                                }

                            }
                        }
                    }
                }
            }
        });

    }
}
