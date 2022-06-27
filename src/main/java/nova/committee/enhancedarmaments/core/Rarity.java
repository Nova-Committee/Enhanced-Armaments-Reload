package nova.committee.enhancedarmaments.core;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.TextFormatting;
import nova.committee.enhancedarmaments.common.config.Config;
import nova.committee.enhancedarmaments.util.RandomCollection;

import java.util.Random;

public enum Rarity {
    DEFAULT("", 0, 0.0, 0.0),
    BASIC(TextFormatting.WHITE, 0xFFFFFF, Config.basicChance, Config.basicDamage),
    UNCOMMON(TextFormatting.DARK_GREEN, 0x00AA00, Config.uncommonChance, Config.uncommonDamage),
    RARE(TextFormatting.AQUA, 0x55FFFF, Config.rareChance, Config.rareDamage),
    ULTRA_RARE(TextFormatting.DARK_PURPLE, 0xAA00AA, Config.ultraRareChance, Config.ultraRareDamage),
    LEGENDARY(TextFormatting.GOLD, 0xFFAA00, Config.legendaryChance, Config.legendaryDamage),
    ARCHAIC(TextFormatting.LIGHT_PURPLE, 0xFF55FF, Config.archaicChance, Config.archaicDamage);

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
    public static Rarity getRarity(CompoundNBT nbt) {
        return nbt != null && nbt.contains("RARITY") ? RARITIES[nbt.getInt("RARITY")] : DEFAULT;
    }

    public static void setRarity(CompoundNBT nbt, int rarityName) {
        nbt.putInt("RARITY", rarityName);
    }

    public void setRarity(CompoundNBT nbt) {

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