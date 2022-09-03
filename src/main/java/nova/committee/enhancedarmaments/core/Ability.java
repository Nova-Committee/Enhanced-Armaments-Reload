package nova.committee.enhancedarmaments.core;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import nova.committee.enhancedarmaments.common.config.EAConfig;

import java.util.ArrayList;

public enum Ability {
    // weapon
    // active
    FIRE("weapon", "active", EAConfig.fireAbility, ChatFormatting.RED, 0xFF5555, 1, 3),
    FROST("weapon", "active", EAConfig.frostAbility, ChatFormatting.AQUA, 0x55FFFF, 1, 3),
    POISON("weapon", "active", EAConfig.poisonAbility, ChatFormatting.DARK_GREEN, 0x00AA00, 1, 3),
    INNATE("weapon", "active", EAConfig.innateAbility, ChatFormatting.DARK_RED, 0xAA0000, 2, 3),
    BOMBASTIC("weapon", "active", EAConfig.bombasticAbility, ChatFormatting.GRAY, 0xAAAAAA, 3, 3),
    CRITICAL_POINT("weapon", "active", EAConfig.criticalpointAbility, ChatFormatting.DARK_GRAY, 0x555555, 3, 3),
    // passive
    ILLUMINATION("weapon", "passive", EAConfig.illuminationAbility, ChatFormatting.YELLOW, 0xFFFF55, 2, 1),
    ETHEREAL("weapon", "passive", EAConfig.etherealAbility, ChatFormatting.GREEN, 0x55FF55, 2, 2),
    BLOODTHIRST("weapon", "passive", EAConfig.bloodthirstAbility, ChatFormatting.DARK_PURPLE, 0xAA00AA, 3, 2),

    // armor
    // active
    MOLTEN("armor", "active", EAConfig.moltenAbility, ChatFormatting.RED, 0xFF5555, 2, 2),
    FROZEN("armor", "active", EAConfig.frozenAbility, ChatFormatting.AQUA, 0x55FFFF, 2, 2),
    TOXIC("armor", "active", EAConfig.toxicAbility, ChatFormatting.DARK_GREEN, 0x00AA00, 2, 2),
    // passive
    BEASTIAL("armor", "passive", EAConfig.beastialAbility, ChatFormatting.DARK_RED, 0xAA0000, 2, 1),
    REMEDIAL("armor", "passive", EAConfig.remedialAbility, ChatFormatting.LIGHT_PURPLE, 0xFF55FF, 2, 2),
    HARDENED("armor", "passive", EAConfig.hardenedAbility, ChatFormatting.GRAY, 0xAAAAAA, 3, 1),
    ADRENALINE("armor", "passive", EAConfig.adrenalineAbility, ChatFormatting.GREEN, 0x55FF55, 3, 1),
    FROSTWALKER("armor", "passive", EAConfig.frostwalkerAbility, ChatFormatting.DARK_BLUE, 0x55FF55, 3, 2),
    GALLOP("armor", "passive", true, ChatFormatting.BLUE, 0xFF5555, 3, 2);


    public static final ArrayList<Ability> WEAPON_ABILITIES = new ArrayList<Ability>();
    public static final ArrayList<Ability> ARMOR_ABILITIES = new ArrayList<Ability>();
    public static final ArrayList<Ability> ALL_ABILITIES = new ArrayList<Ability>();
    public static int WEAPON_ABILITIES_COUNT = 0;
    public static int ARMOR_ABILITIES_COUNT = 0;

    static {
        for (int i = 0; i < Ability.values().length; i++) {
            Ability.ALL_ABILITIES.add(Ability.values()[i]);
            if (Ability.values()[i].getCategory().equals("weapon") && Ability.values()[i].enabled) {
                Ability.WEAPON_ABILITIES.add(Ability.values()[i]);
                Ability.WEAPON_ABILITIES_COUNT++;
            } else if (Ability.values()[i].getCategory().equals("armor") && Ability.values()[i].enabled) {
                Ability.ARMOR_ABILITIES.add(Ability.values()[i]);
                Ability.ARMOR_ABILITIES_COUNT++;
            }
        }
    }

    private final String category;
    private final String type;
    private final boolean enabled;
    private final String color;
    private final int hex;
    private final int tier;
    private final int maxlevel;

    Ability(String category, String type, boolean enabled, Object color, int hex, int tier, int maxlevel) {
        this.category = category;
        this.type = type;
        this.enabled = enabled;
        this.color = color.toString();
        this.hex = hex;
        this.tier = tier;
        this.maxlevel = maxlevel;
    }

    /**
     * 如果物品有能力，则返回 true。
     *
     * @param nbt nbt
     * @return
     */
    public boolean hasAbility(CompoundTag nbt) {
        return nbt != null && nbt.getInt(toString()) > 0;
    }

    /**
     * 将指定的能力添加到物品中
     *
     * @param nbt nbt
     */
    public void addAbility(CompoundTag nbt) {
        var childTag = new CompoundTag();
        if (nbt.contains("ABILITIES")){
            childTag = nbt.getCompound("ABILITIES");
            childTag.putInt("count", childTag.getInt("count") + 1);
            //nbt.putInt("ABILITIES", nbt.getInt("ABILITIES") + 1);
        }
        else {
            childTag.putInt("count", 1);
            //nbt.putInt("ABILITIES", 1);
        }
        childTag.putInt(toString(), 1);
        nbt.put("ABILITIES", childTag);

    }

    /**
     * 从堆叠的 NBT 中移除指定的能力
     *
     * @param nbt nbt
     */
    public void removeAbility(CompoundTag nbt) {
        var childTag = new CompoundTag();
        if (nbt.contains("ABILITIES")){
            childTag = nbt.getCompound("ABILITIES");
            if (childTag.getInt("count") > 0){
                childTag.putInt("count", childTag.getInt("count") - 1);
            }
            childTag.remove(toString());
            nbt.put("ABILITIES", childTag);
        }



    }

    /**
     * 如果玩家有足够的经验来解锁该能力，则返回真
     *
     * @param nbt nbt
     * @param player player
     * @return
     */
    public boolean hasEnoughExp(Player player, CompoundTag nbt) {
        return getExpLevel(nbt) <= player.experienceLevel || player.isCreative();
    }

    /**
     * 返回需要经验等级的能力
     *
     * @param nbt nbt
     * @return exp
     */
    public int getExpLevel(CompoundTag nbt) {
        int requiredExpLevel = 0;
        var childTag = new CompoundTag();
        if (nbt.contains("ABILITIES")){
            childTag = nbt.getCompound("ABILITIES");
            requiredExpLevel = (getTier() + getMaxLevel()) * (childTag.getInt("count") + 1) - 1;
        }
        else
            requiredExpLevel = getTier() + getMaxLevel();
        return requiredExpLevel;
    }

    /**
     * 设置指定能力的等级
     *
     * @param nbt nbt
     * @param level level
     */
    public void setLevel(CompoundTag nbt, int level) {
        var childTag = nbt.getCompound("ABILITIES");
        childTag.putInt(toString(), level);

    }

    /**
     * 返回指定能力的等级。
     *
     * @param nbt nbt
     * @return level
     */
    public int getLevel(CompoundTag nbt) {
        var childTag = new CompoundTag();
        if (nbt != null){
            childTag = nbt.getCompound("ABILITIES");
            return childTag.getInt(toString());
        }
        else
            return 0;
    }

    public boolean canUpgradeLevel(CompoundTag nbt) {
        return getLevel(nbt) < this.maxlevel;
    }

    public int getTier() {
        return tier;
    }

    public int getMaxLevel() {
        return maxlevel;
    }

    public String getColor() {
        return color;
    }

    public int getHex() {
        return hex;
    }

    public String getName() {
        return this.toString();
    }

    public String getName(CompoundTag nbt) {
        if (getLevel(nbt) == 2)
            return Component.translatable("enhancedarmaments.ability." + this).getString() + " II";
        else if (getLevel(nbt) == 3)
            return Component.translatable("enhancedarmaments.ability." + this).getString() + " III";
        else
            return Component.translatable("enhancedarmaments.ability." + this).getString();
    }

    public String getType() {
        return type;
    }

    public String getTypeName() {
        return Component.translatable("enhancedarmaments.ability.type." + type).getString();
    }

    public String getCategory() {
        return category;
    }
}
