package nova.committee.enhancedarmaments.init.handler;

import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nova.committee.enhancedarmaments.common.config.Config;
import nova.committee.enhancedarmaments.core.Ability;
import nova.committee.enhancedarmaments.core.Experience;
import nova.committee.enhancedarmaments.core.Rarity;
import nova.committee.enhancedarmaments.util.EAUtil;
import nova.committee.enhancedarmaments.util.NBTUtil;

import java.util.Collection;
import java.util.List;

/**
 * 将鼠标悬停时显示有关武器的信息。
 */
@Mod.EventBusSubscriber
public class ItemTooltipEventHandler {
    /**
     * 每当需要显示工具的提示时调用。
     *
     * @param event
     */
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent(priority = EventPriority.HIGH)
    public void addInformation(ItemTooltipEvent event) {
        List<Component> tooltip = event.getToolTip();
        ItemStack stack = event.getItemStack();
        Item item = stack.getItem();

        if (EAUtil.canEnhance(item)) {
            CompoundTag nbt = NBTUtil.loadStackNBT(stack);

            if (Experience.isEnabled(nbt)) {
                Rarity rarity = Rarity.getRarity(nbt);
                int level = Experience.getLevel(nbt);
                int experience = Experience.getExperience(nbt);
                int maxExperience = Experience.getMaxLevelExp(level);

                changeTooltips(tooltip, stack, rarity);

                // add tooltips

                // level
                if (level >= Config.maxLevel)
                    tooltip.add(new TextComponent(I18n.get("enhancedarmaments.misc.level") + ": " + ChatFormatting.RED + I18n.get("enhancedarmaments.misc.max")));
                else
                    tooltip.add(new TextComponent(I18n.get("enhancedarmaments.misc.level") + ": " + ChatFormatting.WHITE + level));

                // experience
                if (level >= Config.maxLevel)
                    tooltip.add(new TextComponent(I18n.get("enhancedarmaments.misc.experience") + ": " + I18n.get("enhancedarmaments.misc.max")));
                else
                    tooltip.add(new TextComponent(I18n.get("enhancedarmaments.misc.experience") + ": " + experience + " / " + maxExperience));

                // durability
                if (Config.showDurabilityInTooltip) {
                    tooltip.add(new TextComponent(I18n.get("enhancedarmaments.misc.durability") + ": " + (stack.getMaxDamage() - stack.getDamageValue()) + " / " + stack.getMaxDamage()));
                }

                // abilities
                tooltip.add(new TextComponent(""));
                if (Screen.hasShiftDown()) {
                    tooltip.add(new TextComponent(rarity.getColor() + "" + ChatFormatting.ITALIC + I18n.get("enhancedarmaments.misc.abilities")));
                    tooltip.add(new TextComponent(""));

                    if (EAUtil.canEnhanceWeapon(item)) {
                        for (Ability ability : Ability.WEAPON_ABILITIES) {
                            if (ability.hasAbility(nbt)) {
                                tooltip.add(new TranslatableComponent("-" + ability.getColor() + ability.getName(nbt)));
                            }
                        }
                    } else if (EAUtil.canEnhanceArmor(item)) {
                        for (Ability ability : Ability.ARMOR_ABILITIES) {
                            if (ability.hasAbility(nbt)) {
                                tooltip.add(new TranslatableComponent("-" + ability.getColor() + ability.getName(nbt)));
                            }
                        }
                    }
                } else
                    tooltip.add(new TextComponent(rarity.getColor() + "" + ChatFormatting.ITALIC + I18n.get("enhancedarmaments.misc.abilities.shift")));
            }
        }
    }

    private void changeTooltips(List<Component> tooltip, ItemStack stack, Rarity rarity) {
        // rarity after the name
        tooltip.set(0, new TextComponent(stack.getDisplayName().getString() + rarity.getColor() + " (" + ChatFormatting.ITALIC + I18n.get("enhancedarmaments.rarity." + rarity.getName()) + ")"));

        if (EAUtil.containsString(tooltip, I18n.get("enhancedarmaments.misc.pos.mainHand")) && !(stack.getItem() instanceof BowItem)) {
            Multimap<Attribute, AttributeModifier> map = stack.getItem().getAttributeModifiers(EquipmentSlot.MAINHAND, stack);
            Collection<AttributeModifier> damageCollection = map.get(Attributes.ATTACK_DAMAGE);
            AttributeModifier damageModifier = (AttributeModifier) damageCollection.toArray()[0];
            double damage = ((damageModifier.getAmount() + 1) * rarity.getEffect()) + damageModifier.getAmount() + 1;
            String d = String.format("%.1f", damage);

            if (rarity.getEffect() != 0)
                tooltip.set(EAUtil.lineContainsString(tooltip, I18n.get("enhancedarmaments.misc.pos.mainHand")) + 2, new TextComponent(rarity.getColor() + " " + d + ChatFormatting.GRAY + " " + I18n.get("enhancedarmaments.misc.tooltip.attackdamage")));
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
                tooltip.add(line + 1, new TextComponent(" " + ChatFormatting.BLUE + "+" + rarity.getColor() + percentage + ChatFormatting.BLUE + "% " + I18n.get("enhancedarmaments.misc.rarity.armorreduction")));
        }

        if (EAUtil.canEnhanceRanged(stack.getItem()) && rarity.getEffect() != 0) {
            String b = String.format("%.1f", rarity.getEffect() / 3 * 100);
            tooltip.add(1, new TextComponent(I18n.get("enhancedarmaments.misc.rarity.arrowpercentage") + " " + rarity.getColor() + "+" + b + "%"));
        }
    }
}