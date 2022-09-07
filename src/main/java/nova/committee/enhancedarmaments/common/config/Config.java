package nova.committee.enhancedarmaments.common.config;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/6/27 22:08
 * Version: 1.0
 */

public class Config {

    @Expose
    public int maxLevel = 10;
    @Expose
    public int level1Experience = 100;
    @Expose
    public double experienceMultiplier = 1.8;
    @Expose
    public boolean showDurabilityInTooltip = true;
    @Expose
    public List<Item> itemBlacklist = new ArrayList<>();
    @Expose
    public List<Item> itemWhitelist = new ArrayList<>();
    @Expose
    public List<Item> extraItems = new ArrayList<>();
    @Expose
    public boolean onlyModdedItems = false;
    @Expose
    public boolean fireAbility = true;
    @Expose
    public boolean frostAbility = true;
    @Expose
    public boolean poisonAbility = true;
    @Expose
    public boolean innateAbility = true;
    @Expose
    public boolean bombasticAbility = true;
    @Expose
    public boolean criticalpointAbility = true;
    @Expose
    public boolean illuminationAbility = true;
    @Expose
    public boolean etherealAbility = true;
    @Expose
    public boolean bloodthirstAbility = true;
    @Expose
    public boolean moltenAbility = true;
    @Expose
    public boolean frozenAbility = true;
    @Expose
    public boolean toxicAbility = true;
    @Expose
    public boolean adrenalineAbility = true;
    @Expose
    public boolean beastialAbility = true;
    @Expose
    public boolean remedialAbility = true;
    @Expose
    public boolean hardenedAbility = true;
    @Expose
    public double firechance = 4d;
    @Expose
    public double frostchance = 4d;
    @Expose
    public double poisonchance = 4d;
    @Expose
    public double innatechance = 4d;
    @Expose
    public double bombasticchance = 4d;
    @Expose
    public double criticalpointchance = 4d;
    @Expose
    public double moltenchance = 4d;
    @Expose
    public double frozenchance = 4d;
    @Expose
    public double toxicchance = 4d;
    @Expose
    public double adrenalinechance = 4d;
    @Expose
    public double hardenedchance = 4d;
    @Expose
    public double basicChance = 0.5;
    @Expose
    public double uncommonChance = 0.18;
    @Expose
    public double rareChance = 0.1;
    @Expose
    public double ultraRareChance = 0.05;
    @Expose
    public double legendaryChance = 0.02;
    @Expose
    public double archaicChance = 0.01;
    @Expose
    public double basicDamage = 0D;
    @Expose
    public double uncommonDamage = 0.155;
    @Expose
    public double rareDamage = 0.155;
    @Expose
    public double ultraRareDamage = 0.38;
    @Expose
    public double legendaryDamage = 0.57;
    @Expose
    public double archaicDamage = 0.9;

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public int getLevel1Experience() {
        return level1Experience;
    }

    public void setLevel1Experience(int level1Experience) {
        this.level1Experience = level1Experience;
    }

    public double getExperienceMultiplier() {
        return experienceMultiplier;
    }

    public void setExperienceMultiplier(double experienceMultiplier) {
        this.experienceMultiplier = experienceMultiplier;
    }

    public boolean isShowDurabilityInTooltip() {
        return showDurabilityInTooltip;
    }

    public void setShowDurabilityInTooltip(boolean showDurabilityInTooltip) {
        this.showDurabilityInTooltip = showDurabilityInTooltip;
    }

    public List<Item> getItemBlacklist() {
        return itemBlacklist;
    }

    public void setItemBlacklist(List<Item> itemBlacklist) {
        this.itemBlacklist = itemBlacklist;
    }

    public List<Item> getItemWhitelist() {
        return itemWhitelist;
    }

    public void setItemWhitelist(List<Item> itemWhitelist) {
        this.itemWhitelist = itemWhitelist;
    }

    public List<Item> getExtraItems() {
        return extraItems;
    }

    public void setExtraItems(List<Item> extraItems) {
        this.extraItems = extraItems;
    }

    public boolean isOnlyModdedItems() {
        return onlyModdedItems;
    }

    public void setOnlyModdedItems(boolean onlyModdedItems) {
        this.onlyModdedItems = onlyModdedItems;
    }

    public boolean isFireAbility() {
        return fireAbility;
    }

    public void setFireAbility(boolean fireAbility) {
        this.fireAbility = fireAbility;
    }

    public boolean isFrostAbility() {
        return frostAbility;
    }

    public void setFrostAbility(boolean frostAbility) {
        this.frostAbility = frostAbility;
    }

    public boolean isPoisonAbility() {
        return poisonAbility;
    }

    public void setPoisonAbility(boolean poisonAbility) {
        this.poisonAbility = poisonAbility;
    }

    public boolean isInnateAbility() {
        return innateAbility;
    }

    public void setInnateAbility(boolean innateAbility) {
        this.innateAbility = innateAbility;
    }

    public boolean isBombasticAbility() {
        return bombasticAbility;
    }

    public void setBombasticAbility(boolean bombasticAbility) {
        this.bombasticAbility = bombasticAbility;
    }

    public boolean isCriticalpointAbility() {
        return criticalpointAbility;
    }

    public void setCriticalpointAbility(boolean criticalpointAbility) {
        this.criticalpointAbility = criticalpointAbility;
    }

    public boolean isIlluminationAbility() {
        return illuminationAbility;
    }

    public void setIlluminationAbility(boolean illuminationAbility) {
        this.illuminationAbility = illuminationAbility;
    }

    public boolean isEtherealAbility() {
        return etherealAbility;
    }

    public void setEtherealAbility(boolean etherealAbility) {
        this.etherealAbility = etherealAbility;
    }

    public boolean isBloodthirstAbility() {
        return bloodthirstAbility;
    }

    public void setBloodthirstAbility(boolean bloodthirstAbility) {
        this.bloodthirstAbility = bloodthirstAbility;
    }

    public boolean isMoltenAbility() {
        return moltenAbility;
    }

    public void setMoltenAbility(boolean moltenAbility) {
        this.moltenAbility = moltenAbility;
    }

    public boolean isFrozenAbility() {
        return frozenAbility;
    }

    public void setFrozenAbility(boolean frozenAbility) {
        this.frozenAbility = frozenAbility;
    }

    public boolean isToxicAbility() {
        return toxicAbility;
    }

    public void setToxicAbility(boolean toxicAbility) {
        this.toxicAbility = toxicAbility;
    }

    public boolean isAdrenalineAbility() {
        return adrenalineAbility;
    }

    public void setAdrenalineAbility(boolean adrenalineAbility) {
        this.adrenalineAbility = adrenalineAbility;
    }

    public boolean isBeastialAbility() {
        return beastialAbility;
    }

    public void setBeastialAbility(boolean beastialAbility) {
        this.beastialAbility = beastialAbility;
    }

    public boolean isRemedialAbility() {
        return remedialAbility;
    }

    public void setRemedialAbility(boolean remedialAbility) {
        this.remedialAbility = remedialAbility;
    }

    public boolean isHardenedAbility() {
        return hardenedAbility;
    }

    public void setHardenedAbility(boolean hardenedAbility) {
        this.hardenedAbility = hardenedAbility;
    }

    public double getFirechance() {
        return firechance;
    }

    public void setFirechance(double firechance) {
        this.firechance = firechance;
    }

    public double getFrostchance() {
        return frostchance;
    }

    public void setFrostchance(double frostchance) {
        this.frostchance = frostchance;
    }

    public double getPoisonchance() {
        return poisonchance;
    }

    public void setPoisonchance(double poisonchance) {
        this.poisonchance = poisonchance;
    }

    public double getInnatechance() {
        return innatechance;
    }

    public void setInnatechance(double innatechance) {
        this.innatechance = innatechance;
    }

    public double getBombasticchance() {
        return bombasticchance;
    }

    public void setBombasticchance(double bombasticchance) {
        this.bombasticchance = bombasticchance;
    }

    public double getCriticalpointchance() {
        return criticalpointchance;
    }

    public void setCriticalpointchance(double criticalpointchance) {
        this.criticalpointchance = criticalpointchance;
    }

    public double getMoltenchance() {
        return moltenchance;
    }

    public void setMoltenchance(double moltenchance) {
        this.moltenchance = moltenchance;
    }

    public double getFrozenchance() {
        return frozenchance;
    }

    public void setFrozenchance(double frozenchance) {
        this.frozenchance = frozenchance;
    }

    public double getToxicchance() {
        return toxicchance;
    }

    public void setToxicchance(double toxicchance) {
        this.toxicchance = toxicchance;
    }

    public double getAdrenalinechance() {
        return adrenalinechance;
    }

    public void setAdrenalinechance(double adrenalinechance) {
        this.adrenalinechance = adrenalinechance;
    }

    public double getHardenedchance() {
        return hardenedchance;
    }

    public void setHardenedchance(double hardenedchance) {
        this.hardenedchance = hardenedchance;
    }

    public double getBasicChance() {
        return basicChance;
    }

    public void setBasicChance(double basicChance) {
        this.basicChance = basicChance;
    }

    public double getUncommonChance() {
        return uncommonChance;
    }

    public void setUncommonChance(double uncommonChance) {
        this.uncommonChance = uncommonChance;
    }

    public double getRareChance() {
        return rareChance;
    }

    public void setRareChance(double rareChance) {
        this.rareChance = rareChance;
    }

    public double getUltraRareChance() {
        return ultraRareChance;
    }

    public void setUltraRareChance(double ultraRareChance) {
        this.ultraRareChance = ultraRareChance;
    }

    public double getLegendaryChance() {
        return legendaryChance;
    }

    public void setLegendaryChance(double legendaryChance) {
        this.legendaryChance = legendaryChance;
    }

    public double getArchaicChance() {
        return archaicChance;
    }

    public void setArchaicChance(double archaicChance) {
        this.archaicChance = archaicChance;
    }

    public double getBasicDamage() {
        return basicDamage;
    }

    public void setBasicDamage(double basicDamage) {
        this.basicDamage = basicDamage;
    }

    public double getUncommonDamage() {
        return uncommonDamage;
    }

    public void setUncommonDamage(double uncommonDamage) {
        this.uncommonDamage = uncommonDamage;
    }

    public double getRareDamage() {
        return rareDamage;
    }

    public void setRareDamage(double rareDamage) {
        this.rareDamage = rareDamage;
    }

    public double getUltraRareDamage() {
        return ultraRareDamage;
    }

    public void setUltraRareDamage(double ultraRareDamage) {
        this.ultraRareDamage = ultraRareDamage;
    }

    public double getLegendaryDamage() {
        return legendaryDamage;
    }

    public void setLegendaryDamage(double legendaryDamage) {
        this.legendaryDamage = legendaryDamage;
    }

    public double getArchaicDamage() {
        return archaicDamage;
    }

    public void setArchaicDamage(double archaicDamage) {
        this.archaicDamage = archaicDamage;
    }
}
