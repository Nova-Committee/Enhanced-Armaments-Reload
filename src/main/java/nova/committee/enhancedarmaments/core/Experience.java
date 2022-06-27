package nova.committee.enhancedarmaments.core;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import nova.committee.enhancedarmaments.common.config.Config;

public class Experience {
    public static int getNextLevel(PlayerEntity player, ItemStack stack, CompoundNBT nbt, int currentLevel, int experience) {
        int newLevel = currentLevel;

        while (currentLevel < Config.CONFIG.maxLevel.get() && experience >= Experience.getMaxLevelExp(currentLevel)) {
            newLevel = currentLevel + 1;
            currentLevel++;
            Experience.setAbilityTokens(nbt, Experience.getAbilityTokens(nbt) + 1);
            player.sendMessage(new StringTextComponent(stack.getDisplayName().getString() + TextFormatting.GRAY + " " + new TranslationTextComponent("enhancedarmaments.misc.level.leveledup").getString() + " " + TextFormatting.GOLD + "" + newLevel + TextFormatting.GRAY + "!"), Util.NIL_UUID);
        }

        return newLevel;
    }

    public static int getLevel(CompoundNBT nbt) {
        return nbt != null ? Math.max(nbt.getInt("LEVEL"), 1) : 1;
    }

    public static boolean canLevelUp(CompoundNBT nbt) {
        return getLevel(nbt) < Config.CONFIG.maxLevel.get();
    }

    public static void setLevel(CompoundNBT nbt, int level) {
        if (nbt != null) {
            if (level > 1)
                nbt.putInt("LEVEL", level);
            else
                nbt.remove("LEVEL");
        }
    }

    public static int getNeededExpForNextLevel(CompoundNBT nbt) {
        return Experience.getMaxLevelExp(Experience.getLevel(nbt)) - Experience.getExperience(nbt);
    }

    public static int getExperience(CompoundNBT nbt) {
        return nbt.contains("EXPERIENCE") ? nbt.getInt("EXPERIENCE") : 0;
    }

    public static void setExperience(CompoundNBT nbt, int experience) {
        if (nbt != null) {
            if (experience > 0)
                nbt.putInt("EXPERIENCE", experience);
            else
                nbt.remove("EXPERIENCE");
        }
    }

    public static int getMaxLevelExp(int level) {
        int maxLevelExp = Config.CONFIG.level1Experience.get();
        for (int i = 1; i < level; i++)
            maxLevelExp *= Config.CONFIG.experienceMultiplier.get();
        return maxLevelExp;
    }

    public static void setAbilityTokens(CompoundNBT nbt, int tokens) {
        if (nbt != null) {
            if (tokens > 0)
                nbt.putInt("TOKENS", tokens);
            else
                nbt.remove("TOKENS");
        }
    }

    public static int getAbilityTokens(CompoundNBT nbt) {
        return nbt != null ? nbt.getInt("TOKENS") : 0;
    }

    public static void enable(CompoundNBT nbt, boolean value) {
        if (nbt != null) {
            if (value)
                nbt.putBoolean("EA_ENABLED", value);
            else
                nbt.remove("EA_ENABLED");
        }
    }

    public static boolean isEnabled(CompoundNBT nbt) {
        return nbt != null && nbt.getBoolean("EA_ENABLED");
    }
}