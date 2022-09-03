package nova.committee.enhancedarmaments.core;

import com.google.gson.JsonObject;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import nova.committee.enhancedarmaments.util.RandomCollection;

import java.util.ArrayList;
import java.util.List;

public class Rarity {

    public static Rarity DEFAULT = new Rarity("default", "", 0, 0, 0);

    public static final List<Rarity> RARITIES = new ArrayList<>();
    public static final RandomCollection<Rarity> RANDOM_RARITIES = new RandomCollection<Rarity>();

    private final String name;
    private final ChatFormatting color;
    private final int hex;
    private final double weight;
    private final double effect;
    private boolean enabled = true;

    Rarity(String name, String color, int hex, double weight, double effect) {
        this.name = name;
        this.color = ChatFormatting.getByName(color);
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
    public static Rarity getRandomRarity(RandomSource random) {
        return RANDOM_RARITIES.next(random);
    }

    private static Rarity getByName(CompoundTag nbt){
        final Rarity[] rarity1 = new Rarity[1];
        RARITIES.stream()
                .filter(rarity -> rarity.getName().equalsIgnoreCase(nbt.getString("RARITY")))
                .findFirst()
                .ifPresent(
                        rarity -> rarity1[0] = rarity);
        return rarity1[0];
    }

    /**
     * 检索应用的稀有度。
     *
     * @param nbt
     * @return
     */
    public static Rarity getRarity(CompoundTag nbt) {
        return nbt != null && nbt.contains("RARITY") ? getByName(nbt) : DEFAULT;
    }

    public static void setRarity(CompoundTag nbt, String rarityName) {
        int rarity = Integer.parseInt(rarityName);
        nbt.putInt("RARITY", rarity);
    }

    public void setRarity(CompoundTag nbt) {
        nbt.putString("RARITY", name);
    }

    public String getName() {
        return name;
    }

    public ChatFormatting getColor() {
        return color;
    }

    public int getHex() {
        return hex;
    }

    public double getEffect() {
        return effect;
    }

    public double getWeight() {
        return weight;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    public static Rarity loadFromJson(JsonObject json) {
        var name1 = GsonHelper.getAsString(json, "name");
        var color1 = GsonHelper.getAsString(json, "color");
        var hex1 = GsonHelper.getAsInt(json, "hex");
        var weight1 = GsonHelper.getAsDouble(json, "weight", 0.25D);
        var effect1 = GsonHelper.getAsDouble(json, "effect", 0.3D);

        var rarity = new Rarity(name1, color1, hex1, weight1, effect1);

        var enabled = GsonHelper.getAsBoolean(json, "enabled", true);

        rarity.setEnabled(enabled);

        return rarity;
    }
}
