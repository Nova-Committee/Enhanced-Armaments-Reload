package nova.committee.enhancedarmaments.common.config;

import com.google.gson.annotations.SerializedName;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import nova.committee.enhancedarmaments.Static;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/6/27 22:08
 * Version: 1.0
 */

public class Config {

    @SerializedName("maxLevel")
    public int maxLevel = 10;
    @SerializedName("level1Experience")
    public int level1Experience = 100;
    @SerializedName("experienceMultiplier")
    public double experienceMultiplier = 1.8;
    @SerializedName("showDurabilityInTooltip")
    public boolean showDurabilityInTooltip = true;
    @SerializedName("itemBlacklist")
    public List<Item> itemBlacklist = new ArrayList<>();
    @SerializedName("itemWhitelist")
    public List<Item> itemWhitelist = new ArrayList<>();
    @SerializedName("extraItems")
    public List<Item> extraItems = new ArrayList<>();
    @SerializedName("onlyModdedItems")
    public boolean onlyModdedItems = true;
    @SerializedName("fireAbility")
    public boolean fireAbility = true;
    @SerializedName("frostAbility")
    public boolean frostAbility = true;
    @SerializedName("poisonAbility")
    public boolean poisonAbility = true;
    @SerializedName("innateAbility")
    public boolean innateAbility = true;
    @SerializedName("bombasticAbility")
    public boolean bombasticAbility = true;
    @SerializedName("criticalpointAbility")
    public boolean criticalpointAbility = true;
    @SerializedName("illuminationAbility")
    public boolean illuminationAbility = true;
    @SerializedName("etherealAbility")
    public boolean etherealAbility = true;
    @SerializedName("bloodthirstAbility")
    public boolean bloodthirstAbility = true;
    @SerializedName("moltenAbility")
    public boolean moltenAbility = true;
    @SerializedName("frozenAbility")
    public boolean frozenAbility = true;
    @SerializedName("toxicAbility")
    public boolean toxicAbility = true;
    @SerializedName("adrenalineAbility")
    public boolean adrenalineAbility = true;
    @SerializedName("beastialAbility")
    public boolean beastialAbility = true;
    @SerializedName("remedialAbility")
    public boolean remedialAbility = true;
    @SerializedName("hardenedAbility")
    public boolean hardenedAbility = true;
    @SerializedName("firechance")
    public double firechance = 4d;
    @SerializedName("frostchance")
    public double frostchance = 4d;
    @SerializedName("poisonchance")
    public double poisonchance = 4d;
    @SerializedName("innatechance")
    public double innatechance = 4d;
    @SerializedName("bombasticchance")
    public double bombasticchance = 4d;
    @SerializedName("criticalpointchance")
    public double criticalpointchance = 4d;
    @SerializedName("moltenchance")
    public double moltenchance = 4d;
    @SerializedName("frozenchance")
    public double frozenchance = 4d;
    @SerializedName("toxicchance")
    public double toxicchance = 4d;
    @SerializedName("adrenalinechance")
    public double adrenalinechance = 4d;
    @SerializedName("hardenedchance")
    public double hardenedchance = 4d;
    @SerializedName("basicChance")
    public double basicChance = 0.5;
    @SerializedName("uncommonChance")
    public double uncommonChance = 0.18;
    @SerializedName("rareChance")
    public double rareChance = 0.1;
    @SerializedName("ultraRareChance")
    public double ultraRareChance = 0.05;
    @SerializedName("legendaryChance")
    public double legendaryChance = 0.02;
    @SerializedName("archaicChance")
    public double archaicChance = 0.01;
    @SerializedName("basicDamage")
    public double basicDamage = 0D;
    @SerializedName("uncommonDamage")
    public double uncommonDamage = 0.155;
    @SerializedName("rareDamage")
    public double rareDamage = 0.155;
    @SerializedName("ultraRareDamage")
    public double ultraRareDamage = 0.38;
    @SerializedName("legendaryDamage")
    public double legendaryDamage = 0.57;
    @SerializedName("archaicDamage")
    public double archaicDamage = 0.9;

    private static List<Item> parseItemList(List<String> lst) {
        List<Item> exp = new ArrayList<>(lst.size());
        for (String s : lst) {
            Item i = Registry.ITEM.get(new ResourceLocation(s));
            if (i == Items.AIR) {
                Static.LOGGER.error("Invalid config entry {} will be ignored from blacklist.", s);
                continue;
            }
            exp.add(i);
        }
        return exp;
    }


}
