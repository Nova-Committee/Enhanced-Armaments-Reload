package nova.committee.enhancedarmaments.common.network;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import nova.committee.enhancedarmaments.core.Ability;
import nova.committee.enhancedarmaments.core.Experience;
import nova.committee.enhancedarmaments.util.EAUtil;
import nova.committee.enhancedarmaments.util.NBTUtil;

import java.util.function.Supplier;

public class GuiAbilityPacket {
    private int index;

    public GuiAbilityPacket(int index) {
        this.index = index;
    }

    public static void encode(GuiAbilityPacket msg, FriendlyByteBuf buffer) {
        buffer.writeInt(msg.index);
    }

    public static GuiAbilityPacket decode(FriendlyByteBuf buf) {
        return new GuiAbilityPacket(
                buf.readInt()
        );
    }

    public static void handle(GuiAbilityPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork
                (() -> {
                    Player player = ctx.get().getSender();

                    if (player != null) {
                        ItemStack stack = player.getInventory().getSelected();

                        if (stack != ItemStack.EMPTY) {
                            CompoundTag nbt = NBTUtil.loadStackNBT(stack);

                            if (EAUtil.canEnhanceWeapon(stack.getItem())) {
                                if (Ability.WEAPON_ABILITIES.get(msg.index).hasAbility(nbt)) {
                                    Ability.WEAPON_ABILITIES.get(msg.index).setLevel(nbt, Ability.WEAPON_ABILITIES.get(msg.index).getLevel(nbt) + 1);
                                    Experience.setAbilityTokens(nbt, Experience.getAbilityTokens(nbt) - Ability.WEAPON_ABILITIES.get(msg.index).getTier());
                                } else {
                                    Ability.WEAPON_ABILITIES.get(msg.index).addAbility(nbt);
                                    if (!player.isCreative())
                                        player.giveExperienceLevels(-Ability.WEAPON_ABILITIES.get(msg.index).getExpLevel(nbt) + 1);
                                }
                            } else if (EAUtil.canEnhanceArmor(stack.getItem())) {
                                if (Ability.ARMOR_ABILITIES.get(msg.index).hasAbility(nbt)) {
                                    Ability.ARMOR_ABILITIES.get(msg.index).setLevel(nbt, Ability.ARMOR_ABILITIES.get(msg.index).getLevel(nbt) + 1);
                                    Experience.setAbilityTokens(nbt, Experience.getAbilityTokens(nbt) - Ability.ARMOR_ABILITIES.get(msg.index).getTier());
                                } else {
                                    Ability.ARMOR_ABILITIES.get(msg.index).addAbility(nbt);
                                    if (!player.isCreative())
                                        player.giveExperienceLevels(-Ability.ARMOR_ABILITIES.get(msg.index).getExpLevel(nbt) + 1);
                                }
                            }
                        }
                    }
                });
    }
}