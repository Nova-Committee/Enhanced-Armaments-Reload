package nova.committee.enhancedarmaments.init.handler;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import nova.committee.enhancedarmaments.client.gui.AbilitySelectionGui;
import nova.committee.enhancedarmaments.init.callback.KeyInputCallback;
import nova.committee.enhancedarmaments.util.EAUtil;

import static nova.committee.enhancedarmaments.client.EnhancedArmamentsClient.keyBinding;


/**
 * Opens the weapon ability selection gui on key press.
 */
public class InputEventHandler {

    public static void onKeyPress() {
        KeyInputCallback.EVENT.register((key, scancode, action, mods) -> {
            final var pressed = InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), keyBinding.getDefaultKey().getValue());
            if (!pressed) return;

            Minecraft mc = Minecraft.getInstance();
            Player player = mc.player;

            if (player != null) {
                ItemStack stack = player.getInventory().getSelected();

                if (stack != ItemStack.EMPTY
                        && EAUtil.canEnhance(stack.getItem())
                        && stack.hasTag()
                        && stack.getOrCreateTag().contains("EA_ENABLED")) {
                    mc.setScreen(new AbilitySelectionGui());

                }
            }

        });

    }
}
