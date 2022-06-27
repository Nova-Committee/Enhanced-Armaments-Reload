package nova.committee.enhancedarmaments.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class NBTUtil {
    public static CompoundNBT loadStackNBT(ItemStack stack) {
        return stack.getOrCreateTag();
    }

    public static void saveStackNBT(ItemStack stack, CompoundNBT nbt) {
        if (nbt != null) {
            stack.setTag(nbt);
        }
    }
}