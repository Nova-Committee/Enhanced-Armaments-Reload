package nova.committee.enhancedarmaments.util;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import nova.committee.enhancedarmaments.Static;

import java.util.List;
import java.util.Objects;
import java.util.UUID;


public class EAUtil {
    public static final ImmutableList<Item> vanillaItems = ImmutableList.of(Items.IRON_SWORD, Items.IRON_AXE, Items.IRON_HOE, Items.IRON_BOOTS, Items.IRON_CHESTPLATE, Items.IRON_HELMET,
            Items.IRON_LEGGINGS, Items.DIAMOND_AXE, Items.DIAMOND_HOE, Items.DIAMOND_SWORD, Items.DIAMOND_BOOTS, Items.DIAMOND_CHESTPLATE, Items.DIAMOND_HELMET, Items.DIAMOND_LEGGINGS,
            Items.GOLDEN_AXE, Items.GOLDEN_HOE, Items.GOLDEN_SWORD, Items.GOLDEN_BOOTS, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_HELMET, Items.GOLDEN_LEGGINGS,
            Items.STONE_AXE, Items.STONE_HOE, Items.STONE_SWORD, Items.WOODEN_AXE, Items.WOODEN_HOE, Items.WOODEN_SWORD, Items.BOW, Items.CROSSBOW, Items.TRIDENT,
            Items.NETHERITE_AXE, Items.NETHERITE_HOE, Items.NETHERITE_SWORD, Items.NETHERITE_BOOTS, Items.NETHERITE_CHESTPLATE, Items.NETHERITE_HELMET, Items.NETHERITE_LEGGINGS,
            Items.CHAINMAIL_BOOTS, Items.CHAINMAIL_CHESTPLATE, Items.CHAINMAIL_HELMET, Items.CHAINMAIL_LEGGINGS);

    public static boolean canEnhance(Item item) {
        if (Static.config.onlyModdedItems && vanillaItems.contains(item)) return false;

        if (Static.config.extraItems.size() != 0) {
            boolean allowed = false;
            for (int k = 0; k < Static.config.extraItems.size(); k++)
                if (Objects.equals(Registry.ITEM.getKey(Static.config.extraItems.get(k)), Registry.ITEM.getKey(item)))
                    allowed = true;
            return allowed || item instanceof SwordItem || item instanceof AxeItem || item instanceof HoeItem || item instanceof BowItem || item instanceof ArmorItem || item instanceof CrossbowItem || item instanceof TridentItem;
        } else
            return item instanceof SwordItem || item instanceof AxeItem || item instanceof HoeItem || item instanceof BowItem || item instanceof ArmorItem || item instanceof CrossbowItem || item instanceof TridentItem;
    }

    public static boolean canEnhanceWeapon(Item item) {
        return canEnhance(item) && !(item instanceof ArmorItem);
    }

    /**
     * 近战
     */
    public static boolean canEnhanceMelee(Item item) {
        return canEnhance(item) && !(item instanceof ArmorItem) && !(item instanceof BowItem) && !(item instanceof CrossbowItem);
    }

    /**
     * 远程
     */
    public static boolean canEnhanceRanged(Item item) {
        return canEnhance(item) && (item instanceof BowItem || item instanceof CrossbowItem || item instanceof TridentItem);
    }


    public static boolean canEnhanceArmor(Item item) {
        return canEnhance(item) && item instanceof ArmorItem;
    }


    public static Entity getEntityByUniqueId(UUID uniqueId)
    {
        for (Entity entity : Minecraft.getInstance().level.entitiesForRendering())
        {
            if (entity.getUUID().equals(uniqueId))
                return entity;
        }

        return null;
    }

    public static boolean isDamageSourceAllowed(DamageSource damageSource) {
        return !(damageSource == DamageSource.FALL ||
                damageSource == DamageSource.DROWN ||
                damageSource == DamageSource.CACTUS ||
                damageSource == DamageSource.STARVE ||
                damageSource == DamageSource.IN_WALL ||
                damageSource == DamageSource.IN_FIRE ||
                damageSource == DamageSource.OUT_OF_WORLD) || damageSource.getEntity() instanceof LivingEntity;
    }

    public static boolean containsString(List<Component> tooltip, String string) {
        if (tooltip.size() <= 0) return false;

        for (Component component : tooltip) {
            if (component.getString().equals(string))
                return true;
        }
        return false;
    }

    public static int lineContainsString(List<Component> tooltip, String string) {
        if (tooltip.size() <= 0) return -1;

        for (int i = 0; i < tooltip.size(); i++) {
            if (tooltip.get(i).getString().equals(string))
                return i;
        }
        return -1;
    }
}
