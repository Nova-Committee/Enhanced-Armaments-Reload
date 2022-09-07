package nova.committee.enhancedarmaments.init.handler;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import nova.committee.enhancedarmaments.Static;
import nova.committee.enhancedarmaments.core.Ability;
import nova.committee.enhancedarmaments.core.Experience;
import nova.committee.enhancedarmaments.util.EAUtil;
import nova.committee.enhancedarmaments.util.NBTUtil;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/9/6 19:04
 * Version: 1.0
 */
public class NetWorkHandler {

    public static void onRun(){
        ServerPlayNetworking.registerGlobalReceiver(new ResourceLocation(Static.MOD_ID, "main"),
                (server, player, handler, buf, responseSender) -> {
                    var index = buf.readInt();
                    server.execute(() -> {
                        if (player != null) {
                            var stack = player.getInventory().getSelected();

                            if (stack != ItemStack.EMPTY) {
                                var nbt = NBTUtil.loadStackNBT(stack);

                                if (EAUtil.canEnhanceWeapon(stack.getItem())) {
                                    if (Ability.WEAPON_ABILITIES.get(index).hasAbility(nbt)) {
                                        Ability.WEAPON_ABILITIES.get(index).setLevel(nbt, Ability.WEAPON_ABILITIES.get(index).getLevel(nbt) + 1);
                                        Experience.setAbilityTokens(nbt, Experience.getAbilityTokens(nbt) - Ability.WEAPON_ABILITIES.get(index).getTier());
                                    } else {
                                        Ability.WEAPON_ABILITIES.get(index).addAbility(nbt);
                                        if (!player.isCreative())
                                            player.giveExperienceLevels(-Ability.WEAPON_ABILITIES.get(index).getExpLevel(nbt) + 1);
                                    }
                                } else if (EAUtil.canEnhanceArmor(stack.getItem())) {
                                    if (Ability.ARMOR_ABILITIES.get(index).hasAbility(nbt)) {
                                        Ability.ARMOR_ABILITIES.get(index).setLevel(nbt, Ability.ARMOR_ABILITIES.get(index).getLevel(nbt) + 1);
                                        Experience.setAbilityTokens(nbt, Experience.getAbilityTokens(nbt) - Ability.ARMOR_ABILITIES.get(index).getTier());
                                    } else {
                                        Ability.ARMOR_ABILITIES.get(index).addAbility(nbt);
                                        if (!player.isCreative())
                                            player.giveExperienceLevels(-Ability.ARMOR_ABILITIES.get(index).getExpLevel(nbt) + 1);
                                    }
                                }
                            }
                        }
                    });

                });
    }
}
