package nova.committee.enhancedarmaments.init.handler;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.FrostWalkerEnchantment;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import nova.committee.enhancedarmaments.common.config.EAConfig;
import nova.committee.enhancedarmaments.core.Ability;
import nova.committee.enhancedarmaments.core.Experience;
import nova.committee.enhancedarmaments.core.Rarity;
import nova.committee.enhancedarmaments.util.EAUtil;
import nova.committee.enhancedarmaments.util.NBTUtil;
import java.util.Objects;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LivingUpdateEventHandler {
    private static int count = 0;

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onUpdate(LivingEvent.LivingTickEvent event) {
        if (event.getEntity() instanceof Player player) {
            NonNullList<ItemStack> main = player.getInventory().items;
            if (!player.level().isClientSide()) {
                for (ItemStack stack : player.getInventory().armor) {
                    if (stack != null && EAUtil.canEnhanceArmor(stack.getItem())) {
                        var nbt = NBTUtil.loadStackNBT(stack);

                        //盔甲治愈
                        if (Ability.REMEDIAL.hasAbility(nbt)) {
                            float heal = Ability.REMEDIAL.getLevel(nbt);
                            if (count < 120) {
                                count++;
                            } else {
                                count = 0;
                                player.heal(heal);
                            }
                        }
                        //寒霜行者
                        if (Ability.FROSTWALKER.hasAbility(nbt) && (int) (Math.random() * EAConfig.frostwalkerchance) == 0) {
                            int multiplier = Ability.FROSTWALKER.getLevel(nbt);
                            FrostWalkerEnchantment.onEntityMoved(player, player.level(), player.blockPosition(), multiplier);
                        }

                    }
                }
                for (ItemStack stack : main) {
                    if (stack != ItemStack.EMPTY) {
                        var item = stack.getItem();

                        if (EAUtil.canEnhance(item)) {
                            CompoundTag nbt = NBTUtil.loadStackNBT(stack);
                            if (!Experience.isEnabled(nbt)) {
                                boolean okay = true;

                                //黑名单检测
                                for (int j = 0; j < EAConfig.itemBlacklist.size(); j++) {
                                    if (Objects.equals(ForgeRegistries.ITEMS.getKey(EAConfig.itemBlacklist.get(j)), ForgeRegistries.ITEMS.getKey(stack.getItem())))
                                        okay = false;
                                }

                                //白名单检测
                                if (!Config.itemWhitelist.isEmpty()) {
                                    okay = false;
                                    for (int k = 0; k < EAConfig.itemWhitelist.size(); k++)
                                        if (Objects.equals(ForgeRegistries.ITEMS.getKey(EAConfig.itemWhitelist.get(k)), ForgeRegistries.ITEMS.getKey(stack.getItem())))
                                            okay = true;
                                }

                                if (okay) {
                                    //添加随机稀有度词条
                                    Experience.enable(nbt, true);
                                    Rarity rarity = Rarity.getRarity(nbt);
                                    RandomSource rand = player.level().random;
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
