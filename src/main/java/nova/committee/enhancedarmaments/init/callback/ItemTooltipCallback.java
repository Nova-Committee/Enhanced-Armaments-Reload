package nova.committee.enhancedarmaments.init.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/6/28 0:31
 * Version: 1.0
 */
public interface ItemTooltipCallback {

    public static final Event<ItemTooltipCallback> LIVING_TICK = EventFactory.createArrayBacked(ItemTooltipCallback.class, callbacks -> (itemStack, entityPlayer, list, flags) -> {
        for (ItemTooltipCallback callback : callbacks) {
            callback.onItemTooltip(itemStack, entityPlayer, list, flags);
        }
    });


    void onItemTooltip(ItemStack itemStack, @Nullable Player entityPlayer, List<Component> list, TooltipFlag flags);


}
