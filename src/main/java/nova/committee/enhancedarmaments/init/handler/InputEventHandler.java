package nova.committee.enhancedarmaments.init.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import nova.committee.enhancedarmaments.client.EnhancedArmamentsClient;
import nova.committee.enhancedarmaments.client.gui.AbilitySelectionGui;
import nova.committee.enhancedarmaments.init.callback.KeyInputCallback;
import nova.committee.enhancedarmaments.util.EAUtil;


/**
 * Opens the weapon ability selection gui on key press.
 */
public class InputEventHandler {

    public static void onKeyPress() {
        KeyInputCallback.EVENT.register((key, scancode, action, mods) -> {
            while (EnhancedArmamentsClient.keyBinding.isDown()) {
                Minecraft mc = Minecraft.getInstance();
                Player player = mc.player;

                if (player != null) {
                    ItemStack stack = player.getInventory().getSelected();

                    if (stack != ItemStack.EMPTY) {
                        if (EAUtil.canEnhance(stack.getItem())) {
                            if (stack.hasTag())
                                if (stack.getOrCreateTag().contains("EA_ENABLED"))
                                    mc.setScreen(new AbilitySelectionGui());
                        }
                        else {
                            break;
                        }
                    }
                }
            }
        });

    }
}
