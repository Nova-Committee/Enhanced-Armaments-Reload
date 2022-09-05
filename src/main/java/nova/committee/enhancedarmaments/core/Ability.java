package nova.committee.enhancedarmaments.core;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import nova.committee.enhancedarmaments.Static;

import java.util.ArrayList;

public enum Ability {
    // weapon
    // active
    FIRE("weapon", "active", Static.config.fireAbility, ChatFormatting.RED, 0xFF5555, 1, 3),
    FROST("weapon", "active", Static.config.frostAbility, ChatFormatting.AQUA, 0x55FFFF, 1, 3),
    POISON("weapon", "active", Static.config.poisonAbility, ChatFormatting.DARK_GREEN, 0x00AA00, 1, 3),
    INNATE("weapon", "active", Static.config.innateAbility, ChatFormatting.DARK_RED, 0xAA0000, 2, 3),
    BOMBASTIC("weapon", "active", Static.config.bombasticAbility, ChatFormatting.GRAY, 0xAAAAAA, 3, 3),
    CRITICAL_POINT("weapon", "active", Static.config.criticalpointAbility, ChatFormatting.DARK_GRAY, 0x555555, 3, 3),
    // passive
    ILLUMINATION("weapon", "passive", Static.config.illuminationAbility, ChatFormatting.YELLOW, 0xFFFF55, 2, 1),
    ETHEREAL("weapon", "passive", Static.config.etherealAbility, ChatFormatting.GREEN, 0x55FF55, 2, 2),
    BLOODTHIRST("weapon", "passive", Static.config.bloodthirstAbility, ChatFormatting.DARK_PURPLE, 0xAA00AA, 3, 2),

    // armor
    // active
    MOLTEN("armor", "active", Static.config.moltenAbility, ChatFormatting.RED, 0xFF5555, 2, 2),
    FROZEN("armor", "active", Static.config.frozenAbility, ChatFormatting.AQUA, 0x55FFFF, 2, 2),
    TOXIC("armor", "active", Static.config.toxicAbility, ChatFormatting.DARK_GREEN, 0x00AA00, 2, 2),
    // passive
    BEASTIAL("armor", "passive", Static.config.beastialAbility, ChatFormatting.DARK_RED, 0xAA0000, 2, 1),
    REMEDIAL("armor", "passive", Static.config.remedialAbility, ChatFormatting.LIGHT_PURPLE, 0xFF55FF, 2, 2),
    HARDENED("armor", "passive", Static.config.hardenedAbility, ChatFormatting.GRAY, 0xAAAAAA, 3, 1),
    ADRENALINE("armor", "passive", Static.config.adrenalineAbility, ChatFormatting.GREEN, 0x55FF55, 3, 1);

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

    private String category;
    private String type;
    private boolean enabled;
    private String color;
    private int hex;
    private int tier;
    private int maxlevel;

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
     * 如果堆栈有能力，则返回 true。
     *
     * @param nbt
     * @return
     */
    public boolean hasAbility(CompoundTag nbt) {
        return nbt != null && nbt.getInt(toString()) > 0;
    }

    /**
     * 将指定的能力添加到堆栈中
     *
     * @param nbt
     */
    public void addAbility(CompoundTag nbt) {
        nbt.putInt(toString(), 1);
        if (nbt.contains("ABILITIES"))
            nbt.putInt("ABILITIES", nbt.getInt("ABILITIES") + 1);
        else
            nbt.putInt("ABILITIES", 1);
    }

    /**
     * 从堆叠的 NBT 中移除指定的能力
     *
     * @param nbt
     */
    public void removeAbility(CompoundTag nbt) {
        nbt.remove(toString());
        if (nbt.contains("ABILITIES"))
            if (nbt.getInt("ABILITIES") > 0)
                nbt.putInt("ABILITIES", nbt.getInt("ABILITIES") - 1);
    }

    /**
     * 如果玩家有足够的经验来解锁该能力，则返回真
     *
     * @param nbt
     * @param player
     * @return
     */
    public boolean hasEnoughExp(Player player, CompoundTag nbt) {
        return getExpLevel(nbt) <= player.experienceLevel || player.isCreative();
    }

    /**
     * 返回需要经验等级的能力
     *
     * @param nbt
     * @return
     */
    public int getExpLevel(CompoundTag nbt) {
        int requiredExpLevel = 0;
        if (nbt.contains("ABILITIES"))
            requiredExpLevel = (getTier() + getMaxLevel()) * (nbt.getInt("ABILITIES") + 1) - 1;
        else
            requiredExpLevel = getTier() + getMaxLevel();
        return requiredExpLevel;
    }

    /**
     * 设置指定能力的等级
     *
     * @param nbt
     * @param level
     */
    public void setLevel(CompoundTag nbt, int level) {
        nbt.putInt(toString(), level);
    }

    /**
     * 返回指定能力的等级。
     *
     * @param nbt
     * @return
     */
    public int getLevel(CompoundTag nbt) {
        if (nbt != null) return nbt.getInt(toString());
        else return 0;
    }

    public boolean canUpgradeLevel(CompoundTag nbt) {
        if (getLevel(nbt) < this.maxlevel)
            return true;
        else
            return false;
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
            return new TranslatableComponent("enhancedarmaments.ability." + this.toString()).getString() + " II";
        else if (getLevel(nbt) == 3)
            return new TranslatableComponent("enhancedarmaments.ability." + this.toString()).getString() + " III";
        else
            return new TranslatableComponent("enhancedarmaments.ability." + this.toString()).getString();
    }

    public String getType() {
        return type;
    }

    public String getTypeName() {
        return new TranslatableComponent("enhancedarmaments.ability.type." + type.toString()).getString();
    }

    public String getCategory() {
        return category;
    }
}
