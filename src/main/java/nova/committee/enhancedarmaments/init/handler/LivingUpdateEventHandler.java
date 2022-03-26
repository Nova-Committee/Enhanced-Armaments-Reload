package nova.committee.enhancedarmaments.init.handler;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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

@Mod.EventBusSubscriber
public class LivingUpdateEventHandler {
    //this needs to be a player capability or this will be really random in Multiplayer!
    private int count = 0;

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving() instanceof Player) {
            Player player = (Player) event.getEntityLiving();

            if (player != null) {
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