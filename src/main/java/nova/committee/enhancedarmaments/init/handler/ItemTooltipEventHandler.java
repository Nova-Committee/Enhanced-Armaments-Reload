package nova.committee.enhancedarmaments.init.handler;

import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import nova.committee.enhancedarmaments.Static;
import nova.committee.enhancedarmaments.core.Ability;
import nova.committee.enhancedarmaments.core.Experience;
import nova.committee.enhancedarmaments.core.Rarity;
import nova.committee.enhancedarmaments.init.callback.ItemTooltipCallback;
import nova.committee.enhancedarmaments.util.EAUtil;
import nova.committee.enhancedarmaments.util.NBTUtil;

import java.util.Collection;
import java.util.List;

/**
 * 将鼠标悬停时显示有关武器的信息。
 */
public class ItemTooltipEventHandler {
    /**
     * 每当需要显示工具的提示时调用。
     */
    public static void addInformation() {
        ItemTooltipCallback.LIVING_TICK.register((itemStack, entityPlayer, list, flags) -> {
            Item item = itemStack.getItem();

            if (EAUtil.canEnhance(item)) {
                CompoundTag nbt = NBTUtil.loadStackNBT(itemStack);

                if (Experience.isEnabled(nbt)) {
                    Rarity rarity = Rarity.getRarity(nbt);
                    int level = Experience.getLevel(nbt);
                    int experience = Experience.getExperience(nbt);
                    int maxExperience = Experience.getMaxLevelExp(level);

                    changeTooltips(list, itemStack, rarity);

                    // add tooltips

                    // level
                    if (level >= Static.config.maxLevel)
                        list.add(Component.literal(I18n.get("enhancedarmaments.misc.level") + ": " + ChatFormatting.RED + I18n.get("enhancedarmaments.misc.max")));
                    else
                        list.add(Component.literal(I18n.get("enhancedarmaments.misc.level") + ": " + ChatFormatting.WHITE + level));

                    // experience
                    if (level >= Static.config.maxLevel)
                        list.add(Component.literal(I18n.get("enhancedarmaments.misc.experience") + ": " + I18n.get("enhancedarmaments.misc.max")));
                    else
                        list.add(Component.literal(I18n.get("enhancedarmaments.misc.experience") + ": " + experience + " / " + maxExperience));

                    // durability
                    if (Static.config.showDurabilityInTooltip) {
                        list.add(Component.literal(I18n.get("enhancedarmaments.misc.durability") + ": " + (itemStack.getMaxDamage() - itemStack.getDamageValue()) + " / " + itemStack.getMaxDamage()));
                    }

                    // abilities
                    list.add(Component.literal(""));
                    if (Screen.hasShiftDown()) {
                        list.add(Component.literal(rarity.getColor() + "" + ChatFormatting.ITALIC + I18n.get("enhancedarmaments.misc.abilities")));
                        list.add(Component.literal(""));

                        if (EAUtil.canEnhanceWeapon(item)) {
                            for (Ability ability : Ability.WEAPON_ABILITIES) {
                                if (ability.hasAbility(nbt)) {
                                    list.add(Component.translatable("-" + ability.getColor() + ability.getName(nbt)));
                                }
                            }
                        } else if (EAUtil.canEnhanceArmor(item)) {
                            for (Ability ability : Ability.ARMOR_ABILITIES) {
                                if (ability.hasAbility(nbt)) {
                                    list.add(Component.translatable("-" + ability.getColor() + ability.getName(nbt)));
                                }
                            }
                        }
                    } else
                        list.add(Component.literal(rarity.getColor() + "" + ChatFormatting.ITALIC + I18n.get("enhancedarmaments.misc.abilities.shift")));
                }
            }
        });

    }

    private static void changeTooltips(List<Component> tooltip, ItemStack stack, Rarity rarity) {
        // rarity after the name
        tooltip.set(0, Component.literal(stack.getDisplayName().getString() + rarity.getColor() + " (" + ChatFormatting.ITALIC + I18n.get("enhancedarmaments.rarity." + rarity.getName()) + ")"));

        if (EAUtil.containsString(tooltip, I18n.get("enhancedarmaments.misc.pos.mainHand")) && !(stack.getItem() instanceof BowItem)) {
            Multimap<Attribute, AttributeModifier> map = stack.getItem().getAttributeModifiers(stack, EquipmentSlot.MAINHAND);
            Collection<AttributeModifier> damageCollection = map.get(Attributes.ATTACK_DAMAGE);
            AttributeModifier damageModifier = (AttributeModifier) damageCollection.toArray()[0];
            double damage = ((damageModifier.getAmount() + 1) * rarity.getEffect()) + damageModifier.getAmount() + 1;
            String d = String.format("%.1f", damage);

            if (rarity.getEffect() != 0)
                tooltip.set(EAUtil.lineContainsString(tooltip, I18n.get("enhancedarmaments.misc.pos.mainHand")) + 2, Component.literal(rarity.getColor() + " " + d + ChatFormatting.GRAY + " " + I18n.get("enhancedarmaments.misc.tooltip.attackdamage")));
        }

        if (EAUtil.containsString(tooltip, I18n.get("enhancedarmaments.misc.pos.head")) || EAUtil.containsString(tooltip, I18n.get("enhancedarmaments.misc.pos.body")) || EAUtil.containsString(tooltip, I18n.get("enhancedarmaments.misc.pos.legs")) || EAUtil.containsString(tooltip, I18n.get("enhancedarmaments.misc.pos.feet"))) {
            String p = String.format("%.1f", 100 - (100 / (1.0F + (rarity.getEffect() / 5F))));
            float percentage = Float.parseFloat(p);
            int line = 2;
            if (EAUtil.containsString(tooltip, I18n.get("enhancedarmaments.misc.pos.head")))
                line = EAUtil.lineContainsString(tooltip, I18n.get("enhancedarmaments.misc.pos.head"));
            if (EAUtil.containsString(tooltip, I18n.get("enhancedarmaments.misc.pos.body")))
                line = EAUtil.lineContainsString(tooltip, I18n.get("enhancedarmaments.misc.pos.body"));
            if (EAUtil.containsString(tooltip, I18n.get("enhancedarmaments.misc.pos.legs")))
                line = EAUtil.lineContainsString(tooltip, I18n.get("enhancedarmaments.misc.pos.legs"));
            if (EAUtil.containsString(tooltip, I18n.get("enhancedarmaments.misc.pos.feet")))
                line = EAUtil.lineContainsString(tooltip, I18n.get("enhancedarmaments.misc.pos.feet"));
            if (percentage != 0)
                tooltip.add(line + 1, Component.literal(" " + ChatFormatting.BLUE + "+" + rarity.getColor() + percentage + ChatFormatting.BLUE + "% " + I18n.get("enhancedarmaments.misc.rarity.armorreduction")));
        }

        if (EAUtil.canEnhanceRanged(stack.getItem()) && rarity.getEffect() != 0) {
            String b = String.format("%.1f", rarity.getEffect() / 3 * 100);
            tooltip.add(1, Component.literal(I18n.get("enhancedarmaments.misc.rarity.arrowpercentage") + " " + rarity.getColor() + "+" + b + "%"));
        }
    }
}
