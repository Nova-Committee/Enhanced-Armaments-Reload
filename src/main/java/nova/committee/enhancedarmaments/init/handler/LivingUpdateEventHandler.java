package nova.committee.enhancedarmaments.init.handler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nova.committee.enhancedarmaments.common.config.Config;
import nova.committee.enhancedarmaments.core.Ability;
import nova.committee.enhancedarmaments.core.Experience;
import nova.committee.enhancedarmaments.core.Rarity;
import nova.committee.enhancedarmaments.util.EAUtil;
import nova.committee.enhancedarmaments.util.NBTUtil;

import java.util.Objects;
import java.util.Random;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LivingUpdateEventHandler {
    private static int count = 0;

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();

            if (player != null) {
                NonNullList<ItemStack> main = player.inventory.items;

                if (!player.level.isClientSide) {
                    for (ItemStack stack : player.inventory.armor) {
                        if (stack != null && EAUtil.canEnhanceArmor(stack.getItem())) {
                            CompoundNBT nbt = NBTUtil.loadStackNBT(stack);
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
                                CompoundNBT nbt = NBTUtil.loadStackNBT(stack);
                                if (!Experience.isEnabled(nbt)) {
                                    boolean okay = true;

                                    for (int j = 0; j < Config.itemBlacklist.size(); j++) {
                                        if (Objects.equals(Config.itemBlacklist.get(j).getRegistryName(), stack.getItem().getRegistryName()))
                                            okay = false;
                                    }

                                    if (Config.itemWhitelist.size() != 0) {
                                        okay = false;
                                        for (int k = 0; k < Config.itemWhitelist.size(); k++)
                                            if (Objects.equals(Config.itemWhitelist.get(k).getRegistryName(), stack.getItem().getRegistryName()))
                                                okay = true;
                                    }

                                    if (okay) {
                                        Experience.enable(nbt, true);
                                        Rarity rarity = Rarity.getRarity(nbt);
                                        Random rand = player.level.random;

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
        }
    }
}
