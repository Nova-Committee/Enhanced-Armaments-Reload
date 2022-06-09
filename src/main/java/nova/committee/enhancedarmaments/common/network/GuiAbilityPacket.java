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

public class GuiAbilityPacket extends IPacket {
    private final int index;

    public GuiAbilityPacket(int index) {
        this.index = index;
    }

    public GuiAbilityPacket(FriendlyByteBuf buf) {
        this.index = buf.readInt();
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeInt(index);
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork
                (() -> {
                    Player player = ctx.get().getSender();

                    if (player != null) {
                        ItemStack stack = player.getInventory().getSelected();

                        if (stack != ItemStack.EMPTY) {
                            CompoundTag nbt = NBTUtil.loadStackNBT(stack);

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
    }
}
