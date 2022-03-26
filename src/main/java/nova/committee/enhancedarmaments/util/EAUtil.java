package nova.committee.enhancedarmaments.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.entity.TransientEntitySectionManager;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import nova.committee.enhancedarmaments.common.config.Config;

import java.util.List;
import java.util.Objects;
import java.util.UUID;


public class EAUtil {
    public static boolean canEnhance(Item item) {
        if (Config.onlyModdedItems)
            if (item == Items.IRON_SWORD || item == Items.IRON_AXE || item == Items.IRON_HOE || item == Items.IRON_BOOTS || item == Items.IRON_CHESTPLATE || item == Items.IRON_HELMET || item == Items.IRON_LEGGINGS
                    || item == Items.DIAMOND_AXE || item == Items.DIAMOND_HOE || item == Items.DIAMOND_SWORD || item == Items.DIAMOND_BOOTS || item == Items.DIAMOND_CHESTPLATE || item == Items.DIAMOND_HELMET || item == Items.DIAMOND_LEGGINGS
                    || item == Items.GOLDEN_AXE || item == Items.GOLDEN_HOE || item == Items.GOLDEN_SWORD || item == Items.GOLDEN_BOOTS || item == Items.GOLDEN_CHESTPLATE || item == Items.GOLDEN_HELMET || item == Items.GOLDEN_LEGGINGS
                    || item == Items.STONE_AXE || item == Items.STONE_HOE || item == Items.STONE_SWORD
                    || item == Items.WOODEN_AXE || item == Items.WOODEN_HOE || item == Items.WOODEN_SWORD
                    || item == Items.BOW
                    || item == Items.NETHERITE_AXE || item == Items.NETHERITE_HOE || item == Items.NETHERITE_SWORD || item == Items.NETHERITE_BOOTS || item == Items.NETHERITE_CHESTPLATE || item == Items.NETHERITE_HELMET || item == Items.NETHERITE_LEGGINGS
                    || item == Items.CHAINMAIL_BOOTS || item == Items.CHAINMAIL_CHESTPLATE || item == Items.CHAINMAIL_HELMET || item == Items.CHAINMAIL_LEGGINGS)
                return false;

        if (Config.extraItems.size() != 0) {
            boolean allowed = false;
            for (int k = 0; k < Config.extraItems.size(); k++)
                if (Objects.equals(Config.extraItems.get(k).getRegistryName(), item.getRegistryName()))
                    allowed = true;
            return allowed || item instanceof SwordItem || item instanceof AxeItem || item instanceof HoeItem || item instanceof BowItem || item instanceof ArmorItem;
        } else
            return item instanceof SwordItem || item instanceof AxeItem || item instanceof HoeItem || item instanceof BowItem || item instanceof ArmorItem;
    }

    public static boolean canEnhanceWeapon(Item item) {
        return canEnhance(item) && !(item instanceof ArmorItem);
    }

    public static boolean canEnhanceMelee(Item item) {
        return canEnhance(item) && !(item instanceof ArmorItem) && !(item instanceof BowItem);
    }

    public static boolean canEnhanceRanged(Item item) {
        return canEnhance(item) && item instanceof BowItem;
    }

    public static boolean canEnhanceArmor(Item item) {
        return canEnhance(item) && item instanceof ArmorItem;
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

    public static Entity getEntityByUniqueId(UUID uniqueId) {
        final TransientEntitySectionManager<Entity> entityStorage = ObfuscationReflectionHelper.getPrivateValue(ClientLevel.class, Minecraft.getInstance().level, "f_157637_");
        for (Entity entity : entityStorage.getEntityGetter().getAll()) {
            if (entity.getUUID().equals(uniqueId))
                return entity;
        }

        return null;
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