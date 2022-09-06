package nova.committee.enhancedarmaments.core;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import nova.committee.enhancedarmaments.Static;

public class Experience {
    public static int getNextLevel(Player player, ItemStack stack, CompoundTag nbt, int currentLevel, int experience) {
        int newLevel = currentLevel;

        while (currentLevel < Static.configHandler.getConfig().getMaxLevel() && experience >= Experience.getMaxLevelExp(currentLevel)) {
            newLevel = currentLevel + 1;
            currentLevel++;
            Experience.setAbilityTokens(nbt, Experience.getAbilityTokens(nbt) + 1);
            player.sendSystemMessage(Component.literal(stack.getDisplayName().getString() + ChatFormatting.GRAY + " " + Component.translatable("enhancedarmaments.misc.level.leveledup").getString() + " " + ChatFormatting.GOLD + "" + newLevel + ChatFormatting.GRAY + "!"));
        }

        return newLevel;
    }

    public static int getLevel(CompoundTag nbt) {
        return nbt != null ? Math.max(nbt.getInt("LEVEL"), 1) : 1;
    }

    public static boolean canLevelUp(CompoundTag nbt) {
        return getLevel(nbt) < Static.configHandler.getConfig().getMaxLevel();
    }

    public static void setLevel(CompoundTag nbt, int level) {
        if (nbt != null) {
            if (level > 1)
                nbt.putInt("LEVEL", level);
            else
                nbt.remove("LEVEL");
        }
    }

    public static int getNeededExpForNextLevel(CompoundTag nbt) {
        return Experience.getMaxLevelExp(Experience.getLevel(nbt)) - Experience.getExperience(nbt);
    }

    public static int getExperience(CompoundTag nbt) {
        return nbt.contains("EXPERIENCE") ? nbt.getInt("EXPERIENCE") : 0;
    }

    public static void setExperience(CompoundTag nbt, int experience) {
        if (nbt != null) {
            if (experience > 0)
                nbt.putInt("EXPERIENCE", experience);
            else
                nbt.remove("EXPERIENCE");
        }
    }

    public static int getMaxLevelExp(int level) {
        int maxLevelExp = Static.configHandler.getConfig().getLevel1Experience();
        for (int i = 1; i < level; i++)
            maxLevelExp *= Static.configHandler.getConfig().getExperienceMultiplier();
        return maxLevelExp;
    }

    public static void setAbilityTokens(CompoundTag nbt, int tokens) {
        if (nbt != null) {
            if (tokens > 0)
                nbt.putInt("TOKENS", tokens);
            else
                nbt.remove("TOKENS");
        }
    }

    public static int getAbilityTokens(CompoundTag nbt) {
        return nbt != null ? nbt.getInt("TOKENS") : 0;
    }

    public static void enable(CompoundTag nbt, boolean value) {
        if (nbt != null) {
            if (value)
                nbt.putBoolean("EA_ENABLED", true);
            else
                nbt.remove("EA_ENABLED");
        }
    }

    public static boolean isEnabled(CompoundTag nbt) {
        return nbt != null && nbt.getBoolean("EA_ENABLED");
    }
}
