package nova.committee.enhancedarmaments.init.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import nova.committee.enhancedarmaments.init.callback.PlayerEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/6/27 23:27
 * Version: 1.0
 */
@Mixin(BowItem.class)
public abstract class BowMixin {

    @Shadow public abstract int getUseDuration(ItemStack itemStack);

    @Inject(method = "releaseUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getProjectile(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/ItemStack;", shift = At.Shift.AFTER))
    public void Bow_Loose(ItemStack pStack, Level level, LivingEntity pEntityLiving, int pTimeLeft, CallbackInfo ci){

            Player player = (Player) pEntityLiving;
            boolean flag = player.abilities.instabuild || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, pStack) > 0;
            ItemStack itemstack = player.getProjectile(pStack);
            int i = getUseDuration(pStack) - pTimeLeft;
            i = PlayerEvents.ON_ARROW_LOOSE.invoker().onArrowLoose(player, pStack, level, i, !itemstack.isEmpty() || flag);
            if (i < 0) return;

    }
}
