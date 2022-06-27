package nova.committee.enhancedarmaments.common.config;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;
import nova.committee.enhancedarmaments.EnhancedArmaments;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class Config {
    public static final CFile CONFIG;
    public static final ForgeConfigSpec SPEC;
    public static int maxLevel;
    public static int level1Experience;
    public static double experienceMultiplier;
    public static boolean showDurabilityInTooltip;
    public static List<Item> itemBlacklist;
    public static List<Item> itemWhitelist;
    public static List<Item> extraItems;
    public static boolean onlyModdedItems;
    public static boolean fireAbility;
    public static boolean frostAbility;
    public static boolean poisonAbility;
    public static boolean innateAbility;
    public static boolean bombasticAbility;
    public static boolean criticalpointAbility;
    public static boolean illuminationAbility;
    public static boolean etherealAbility;
    public static boolean bloodthirstAbility;
    public static boolean moltenAbility;
    public static boolean frozenAbility;
    public static boolean toxicAbility;
    public static boolean adrenalineAbility;
    public static boolean beastialAbility;
    public static boolean remedialAbility;
    public static boolean hardenedAbility;
    public static double firechance;
    public static double frostchance;
    public static double poisonchance;
    public static double innatechance;
    public static double bombasticchance;
    public static double criticalpointchance;
    public static double moltenchance;
    public static double frozenchance;
    public static double toxicchance;
    public static double adrenalinechance;
    public static double hardenedchance;
    public static double basicChance;
    public static double uncommonChance;
    public static double rareChance;
    public static double ultraRareChance;
    public static double legendaryChance;
    public static double archaicChance;
    public static double basicDamage;
    public static double uncommonDamage;
    public static double rareDamage;
    public static double ultraRareDamage;
    public static double legendaryDamage;
    public static double archaicDamage;

    static {
        final Pair<CFile, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(CFile::new);
        SPEC = specPair.getRight();
        CONFIG = specPair.getLeft();
    }

    public static void load() {
        maxLevel = CONFIG.maxLevel.get();
        level1Experience = CONFIG.level1Experience.get();
        experienceMultiplier = CONFIG.experienceMultiplier.get();
        showDurabilityInTooltip = CONFIG.showDurabilityInTooltip.get();
        onlyModdedItems = CONFIG.onlyModdedItems.get();

        itemBlacklist = parseItemList(CONFIG.itemBlacklist.get());
        itemWhitelist = parseItemList(CONFIG.itemWhitelist.get());
        extraItems = parseItemList(CONFIG.extraItems.get());

        fireAbility = CONFIG.fireAbility.get();
        frostAbility = CONFIG.frostAbility.get();
        poisonAbility = CONFIG.poisonAbility.get();
        innateAbility = CONFIG.innateAbility.get();
        bombasticAbility = CONFIG.bombasticAbility.get();
        criticalpointAbility = CONFIG.criticalpointAbility.get();
        illuminationAbility = CONFIG.illuminationAbility.get();
        etherealAbility = CONFIG.etherealAbility.get();
        bloodthirstAbility = CONFIG.bloodthirstAbility.get();
        moltenAbility = CONFIG.moltenAbility.get();
        frozenAbility = CONFIG.frozenAbility.get();
        toxicAbility = CONFIG.toxicAbility.get();
        adrenalineAbility = CONFIG.adrenalineAbility.get();
        beastialAbility = CONFIG.beastialAbility.get();
        remedialAbility = CONFIG.remedialAbility.get();
        hardenedAbility = CONFIG.hardenedAbility.get();
        fireAbility = CONFIG.fireAbility.get();

        firechance = CONFIG.firechance.get();
        frostchance = CONFIG.frostchance.get();
        poisonchance = CONFIG.poisonchance.get();
        innatechance = CONFIG.innatechance.get();
        bombasticchance = CONFIG.bombasticchance.get();
        criticalpointchance = CONFIG.criticalpointchance.get();
        moltenchance = CONFIG.moltenchance.get();
        frozenchance = CONFIG.frozenchance.get();
        toxicchance = CONFIG.toxicchance.get();
        adrenalinechance = CONFIG.adrenalinechance.get();
        hardenedchance = CONFIG.hardenedchance.get();

        basicChance = CONFIG.basicChance.get();
        uncommonChance = CONFIG.uncommonChance.get();
        rareChance = CONFIG.rareChance.get();
        ultraRareChance = CONFIG.ultraRareChance.get();
        legendaryChance = CONFIG.legendaryChance.get();
        archaicChance = CONFIG.archaicChance.get();

        basicDamage = CONFIG.basicDamage.get();
        uncommonDamage = CONFIG.uncommonDamage.get();
        rareDamage = CONFIG.rareDamage.get();
        ultraRareDamage = CONFIG.ultraRareDamage.get();
        legendaryDamage = CONFIG.legendaryDamage.get();
        archaicDamage = CONFIG.archaicDamage.get();
    }

    private static List<Item> parseItemList(List<String> lst) {
        List<Item> exp = new ArrayList<>(lst.size());
        for (String s : lst) {
            Item i = ForgeRegistries.ITEMS.getValue(new ResourceLocation(s));
            if (i == null || i == Items.AIR) {
                EnhancedArmaments.LOGGER.error("Invalid config entry {} will be ignored from blacklist.", s);
                continue;
            }
            exp.add(i);
        }
        return exp;
    }

    public static class CFile {
        public ForgeConfigSpec.ConfigValue<Integer> maxLevel;
        public ForgeConfigSpec.ConfigValue<Integer> level1Experience;
        public ForgeConfigSpec.ConfigValue<Double> experienceMultiplier;

        public ForgeConfigSpec.BooleanValue showDurabilityInTooltip;
        public ForgeConfigSpec.ConfigValue<List<String>> itemBlacklist;
        public ForgeConfigSpec.ConfigValue<List<String>> itemWhitelist;
        public ForgeConfigSpec.ConfigValue<List<String>> extraItems;
        public ForgeConfigSpec.BooleanValue onlyModdedItems;

        public ForgeConfigSpec.BooleanValue fireAbility;
        public ForgeConfigSpec.BooleanValue frostAbility;
        public ForgeConfigSpec.BooleanValue poisonAbility;
        public ForgeConfigSpec.BooleanValue innateAbility;
        public ForgeConfigSpec.BooleanValue bombasticAbility;
        public ForgeConfigSpec.BooleanValue criticalpointAbility;
        public ForgeConfigSpec.BooleanValue illuminationAbility;
        public ForgeConfigSpec.BooleanValue etherealAbility;
        public ForgeConfigSpec.BooleanValue bloodthirstAbility;

        public ForgeConfigSpec.BooleanValue moltenAbility;
        public ForgeConfigSpec.BooleanValue frozenAbility;
        public ForgeConfigSpec.BooleanValue toxicAbility;
        public ForgeConfigSpec.BooleanValue adrenalineAbility;
        public ForgeConfigSpec.BooleanValue beastialAbility;
        public ForgeConfigSpec.BooleanValue remedialAbility;
        public ForgeConfigSpec.BooleanValue hardenedAbility;

        public ForgeConfigSpec.ConfigValue<Double> firechance;
        public ForgeConfigSpec.ConfigValue<Double> frostchance;
        public ForgeConfigSpec.ConfigValue<Double> poisonchance;
        public ForgeConfigSpec.ConfigValue<Double> innatechance;
        public ForgeConfigSpec.ConfigValue<Double> bombasticchance;
        public ForgeConfigSpec.ConfigValue<Double> criticalpointchance;
        public ForgeConfigSpec.ConfigValue<Double> moltenchance;
        public ForgeConfigSpec.ConfigValue<Double> frozenchance;
        public ForgeConfigSpec.ConfigValue<Double> toxicchance;
        public ForgeConfigSpec.ConfigValue<Double> adrenalinechance;
        public ForgeConfigSpec.ConfigValue<Double> hardenedchance;

        public ForgeConfigSpec.ConfigValue<Double> basicChance;
        public ForgeConfigSpec.ConfigValue<Double> uncommonChance;
        public ForgeConfigSpec.ConfigValue<Double> rareChance;
        public ForgeConfigSpec.ConfigValue<Double> ultraRareChance;
        public ForgeConfigSpec.ConfigValue<Double> legendaryChance;
        public ForgeConfigSpec.ConfigValue<Double> archaicChance;

        public ForgeConfigSpec.ConfigValue<Double> basicDamage;
        public ForgeConfigSpec.ConfigValue<Double> uncommonDamage;
        public ForgeConfigSpec.ConfigValue<Double> rareDamage;
        public ForgeConfigSpec.ConfigValue<Double> ultraRareDamage;
        public ForgeConfigSpec.ConfigValue<Double> legendaryDamage;
        public ForgeConfigSpec.ConfigValue<Double> archaicDamage;

        public CFile(ForgeConfigSpec.Builder builder) {
            buildMain(builder);
            buildMisc(builder);
            buildAbilities(builder);
            buildAbilityChance(builder);
            buildRarities(builder);
            buildMultiplier(builder);
        }

        private void buildMain(ForgeConfigSpec.Builder builder) {
            builder.push("experience");

            maxLevel = builder
                    .comment("Sets the maximum level cap for weapons and armor. Default: 10")
                    .define("maxLevel", 10);

            level1Experience = builder
                    .comment("The experience amount needed for the first level(1). Default: 100")
                    .define("level1Experience", 100);

            experienceMultiplier = builder
                    .comment("The experience multiplier for each level based on the first level experience. Default: 1.8")
                    .define("experienceMultiplier", 1.8);

            builder.pop();
        }

        private void buildMisc(ForgeConfigSpec.Builder builder) {
            builder.push("miscellaneous");

            showDurabilityInTooltip = builder
                    .comment("Determines whether or not durability will be displayed in tooltips. Default: true")
                    .define("showDurabilityInTooltip", true);

            itemBlacklist = builder
                    .comment("Items in this blacklist will not gain the leveling systems. Useful for very powerful items or potential conflicts. Style should be 'modid:item'")
                    .define("itemBlacklist", new ArrayList<>());

            itemWhitelist = builder
                    .comment("This is item whitelist, basically. If you don't want a whitelist, just leave this empty. If you want a whitelist, fill it with items you want. Style should be 'modid:item'")
                    .define("itemWhitelist", new ArrayList<>());

            extraItems = builder
                    .comment("This is an extra item list to add custom support for such modded items. Be careful on this, it may crash if the item can't be enhanced. Style should be 'modid:item'")
                    .define("extraItems", new ArrayList<>());

            onlyModdedItems = builder
                    .comment("Determines if the vanilla items won't be affected by this mod. Default: false")
                    .define("onlyModdedItems", false);

            builder.pop();
        }

        private void buildAbilities(ForgeConfigSpec.Builder builder) {
            builder.push("abilities");

            // weapons
            fireAbility = builder
                    .comment("Determines whether or not the specific ability will be present in-game. Default: true")
                    .define("fireAbility", true);

            frostAbility = builder
                    .comment("Determines whether or not the specific ability will be present in-game. Default: true")
                    .define("frostAbility", true);

            poisonAbility = builder
                    .comment("Determines whether or not the specific ability will be present in-game. Default: true")
                    .define("poisonAbility", true);

            innateAbility = builder
                    .comment("Determines whether or not the specific ability will be present in-game. Default: true")
                    .define("innateAbility", true);

            bombasticAbility = builder
                    .comment("Determines whether or not the specific ability will be present in-game. Default: true")
                    .define("bombasticAbility", true);

            criticalpointAbility = builder
                    .comment("Determines whether or not the specific ability will be present in-game. Default: true")
                    .define("criticalpointAbility", true);

            illuminationAbility = builder
                    .comment("Determines whether or not the specific ability will be present in-game. Default: true")
                    .define("illuminationAbility", true);

            etherealAbility = builder
                    .comment("Determines whether or not the specific ability will be present in-game. Default: true")
                    .define("etherealAbility", true);

            bloodthirstAbility = builder
                    .comment("Determines whether or not the specific ability will be present in-game. Default: true")
                    .define("bloodthirstAbility", true);

            //armor
            moltenAbility = builder
                    .comment("Determines whether or not the specific ability will be present in-game. Default: true")
                    .define("moltenAbility", true);

            frozenAbility = builder
                    .comment("Determines whether or not the specific ability will be present in-game. Default: true")
                    .define("frozenAbility", true);

            moltenAbility = builder
                    .comment("Determines whether or not the specific ability will be present in-game. Default: true")
                    .define("moltenAbility", true);

            toxicAbility = builder
                    .comment("Determines whether or not the specific ability will be present in-game. Default: true")
                    .define("toxicAbility", true);

            adrenalineAbility = builder
                    .comment("Determines whether or not the specific ability will be present in-game. Default: true")
                    .define("adrenalineAbility", true);

            beastialAbility = builder
                    .comment("Determines whether or not the specific ability will be present in-game. Default: true")
                    .define("beastialAbility", true);

            remedialAbility = builder
                    .comment("Determines whether or not the specific ability will be present in-game. Default: true")
                    .define("remedialAbility", true);

            hardenedAbility = builder
                    .comment("Determines whether or not the specific ability will be present in-game. Default: true")
                    .define("hardenedAbility", true);

            builder.pop();
        }

        private void buildAbilityChance(ForgeConfigSpec.Builder builder) {
            builder.push("abilitychances");

            firechance = builder
                    .comment("Determines how rare the Fire ability will occur. (Higher values=lower occurance) Default: 4")
                    .define("firechance", 4D);

            frostchance = builder
                    .comment("Determines how rare the Frost ability will occur. (Higher values=lower occurance) Default: 4")
                    .define("frostchance", 4d);

            poisonchance = builder
                    .comment("Determines how rare the Poison ability will occur. (Higher values=lower occurance) Default: 4")
                    .define("poisonchance", 4d);

            innatechance = builder
                    .comment("Determines how rare the Innate ability will occur. (Higher values=lower occurance) Default: 4")
                    .define("innatechance", 4d);

            bombasticchance = builder
                    .comment("Determines how rare the Bombasitc ability will occur. (Higher values=lower occurance) Default: 4")
                    .define("bombasticchance", 4d);

            criticalpointchance = builder
                    .comment("Determines how rare the Critical Point ability will occur. (Higher values=lower occurance) Default: 4")
                    .define("criticalpointchance", 4d);

            moltenchance = builder
                    .comment("Determines how rare the Molten ability will occur. (Higher values=lower occurance) Default: 4")
                    .define("moltenchance", 4d);

            frozenchance = builder
                    .comment("Determines how rare the Frozen ability will occur. (Higher values=lower occurance) Default: 4")
                    .define("frozenchance", 4d);

            toxicchance = builder
                    .comment("Determines how rare the Toxic ability will occur. (Higher values=lower occurance) Default: 4")
                    .define("toxicchance", 4d);

            adrenalinechance = builder
                    .comment("Determines how rare the Adrinalin ability will occur. (Higher values=lower occurance) Default: 4")
                    .define("adrenalinechance", 4d);

            hardenedchance = builder
                    .comment("Determines how rare the Harden ability will occur. (Higher values=lower occurance) Default: 4")
                    .define("hardenedchance", 4d);

            builder.pop();
        }

        private void buildRarities(ForgeConfigSpec.Builder builder) {
            builder.push("rarities");

            basicChance = builder
                    .comment("Sets the chance the given rarity will be applied. Default: 0.5")
                    .define("basicChance", 0.5);

            uncommonChance = builder
                    .comment("Sets the chance the given rarity will be applied. Default: 0.18")
                    .define("uncommonChance", 0.18);

            rareChance = builder
                    .comment("Sets the chance the given rarity will be applied. Default: 0.1")
                    .define("rareChance", 0.1);

            ultraRareChance = builder
                    .comment("Sets the chance the given rarity will be applied. Default: 0.05")
                    .define("ultraRareChance", 0.05);

            legendaryChance = builder
                    .comment("Sets the chance the given rarity will be applied. Default: 0.02")
                    .define("legendaryChance", 0.02);

            archaicChance = builder
                    .comment("Sets the chance the given rarity will be applied. Default: 0.01")
                    .define("archaicChance", 0.01);

            builder.pop();
        }

        private void buildMultiplier(ForgeConfigSpec.Builder builder) {
            builder.push("multiplier");

            basicDamage = builder
                    .comment("Sets the effectiveness for the given rarity. Default: 0")
                    .define("basicDamage", 0D);

            uncommonDamage = builder
                    .comment("Sets the effectiveness for the given rarity. Default: 0.155")
                    .define("uncommonDamage", 0.155);

            rareDamage = builder
                    .comment("Sets the effectiveness for the given rarity. Default: 0.305")
                    .define("rareDamage", 0.305);

            ultraRareDamage = builder
                    .comment("Sets the effectiveness for the given rarity. Default: 0.38")
                    .define("ultraRareDamage", 0.38);

            legendaryDamage = builder
                    .comment("Sets the effectiveness for the given rarity. Default: 0.57")
                    .define("legendaryDamage", 0.57);

            archaicDamage = builder
                    .comment("Sets the effectiveness for the given rarity. Default: 0.81")
                    .define("archaicDamage", 0.81);

            builder.pop();
        }
    }
}