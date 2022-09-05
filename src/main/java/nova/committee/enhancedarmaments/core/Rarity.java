package nova.committee.enhancedarmaments.core;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import nova.committee.enhancedarmaments.Static;
import nova.committee.enhancedarmaments.util.RandomCollection;

import java.util.Random;

public enum Rarity {

    DEFAULT("", 0, 0.0, 0.0),
    BASIC(ChatFormatting.WHITE, 0xFFFFFF, Static.config.basicChance, Static.config.basicDamage),
    UNCOMMON(ChatFormatting.DARK_GREEN, 0x00AA00, Static.config.uncommonChance, Static.config.uncommonDamage),
    RARE(ChatFormatting.AQUA, 0x55FFFF, Static.config.rareChance, Static.config.rareDamage),
    ULTRA_RARE(ChatFormatting.DARK_PURPLE, 0xAA00AA, Static.config.ultraRareChance, Static.config.ultraRareDamage),
    LEGENDARY(ChatFormatting.GOLD, 0xFFAA00, Static.config.legendaryChance, Static.config.legendaryDamage),
    ARCHAIC(ChatFormatting.LIGHT_PURPLE, 0xFF55FF, Static.config.archaicChance, Static.config.archaicDamage);

    private static final Rarity[] RARITIES = Rarity.values();
    private static final RandomCollection<Rarity> RANDOM_RARITIES = new RandomCollection<Rarity>();


    static {
        for (Rarity rarity : RARITIES) {
            if (rarity.weight > 0.0D) {
                RANDOM_RARITIES.add(rarity.weight, rarity);
            }
        }
    }

    private String color;
    private int hex;
    private double weight;
    private double effect;

    Rarity(Object color, int hex, double weight, double effect) {
        this.color = color.toString();
        this.hex = hex;
        this.weight = weight;
        this.effect = effect;
    }

    /**
     * 根据权重返回上述枚举之一
     *
     * @param random
     * @return
     */
    public static Rarity getRandomRarity(Random random) {
        return RANDOM_RARITIES.next(random);
    }

    /**
     * 检索应用的稀有度。
     *
     * @param nbt
     * @return
     */
    public static Rarity getRarity(CompoundTag nbt) {
        return nbt != null && nbt.contains("RARITY") ? RARITIES[nbt.getInt("RARITY")] : DEFAULT;
    }

    public static void setRarity(CompoundTag nbt, String rarityName) {
        int rarity = Integer.parseInt(rarityName);
        nbt.putInt("RARITY", rarity);
    }

    public void setRarity(CompoundTag nbt) {

        nbt.putInt("RARITY", ordinal());

    }

    public String getName() {
        return this.toString();
    }

    public String getColor() {
        return color;
    }

    public int getHex() {
        return hex;
    }

    public double getEffect() {
        return effect;
    }
}
