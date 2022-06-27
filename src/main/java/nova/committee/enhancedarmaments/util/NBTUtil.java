package nova.committee.enhancedarmaments.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class NBTUtil {
    public static CompoundTag loadStackNBT(ItemStack stack) {
        return stack.getOrCreateTag();
    }

    public static void saveStackNBT(ItemStack stack, CompoundTag nbt) {
        if (nbt != null) {
            stack.setTag(nbt);
        }
    }
}