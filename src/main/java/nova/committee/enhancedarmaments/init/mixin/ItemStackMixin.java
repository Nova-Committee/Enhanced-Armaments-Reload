package nova.committee.enhancedarmaments.init.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import nova.committee.enhancedarmaments.init.callback.ItemTooltipCallback;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/6/28 0:33
 * Version: 1.0
 */
@Mixin(ItemStack.class)
public class ItemStackMixin {


    @Inject(method = "getTooltipLines", at = @At(value = "RETURN"))
    public void tooltipLines(Player player, TooltipFlag tooltipFlag, CallbackInfoReturnable<List<Component>> cir) {

        ItemTooltipCallback.LIVING_TICK.invoker().onItemTooltip((ItemStack) (Object)this, player, cir.getReturnValue(), tooltipFlag);
    }

}
