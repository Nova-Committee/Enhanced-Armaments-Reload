package nova.committee.enhancedarmaments.init.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/6/27 23:16
 * Version: 1.0
 */
public class PlayerEvents {

    public static final Event<PlayerEvents.Arrow_Loose> ON_ARROW_LOOSE = EventFactory.createArrayBacked(PlayerEvents.Arrow_Loose.class, callbacks -> (player, bow, world, charge, hasAmmo) -> {
        for (PlayerEvents.Arrow_Loose callback : callbacks) {
            int newCharge = callback.onArrowLoose(player, bow, world, charge, hasAmmo);
            if (newCharge != charge) {
                return newCharge;
            }
        }
        return -1;
    });


    @FunctionalInterface
    public interface Arrow_Loose {
        int onArrowLoose(Player player, @Nonnull ItemStack bow, Level world, int charge, boolean hasAmmo);
    }
}
