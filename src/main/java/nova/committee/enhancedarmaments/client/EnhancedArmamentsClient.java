package nova.committee.enhancedarmaments.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import nova.committee.enhancedarmaments.init.handler.InputEventHandler;
import org.lwjgl.glfw.GLFW;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/6/27 21:57
 * Version: 1.0
 */
@Environment(EnvType.CLIENT)
public class EnhancedArmamentsClient implements ClientModInitializer {

    public static KeyMapping keyBinding;

    @Override
    public void onInitializeClient() {
        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.gui.weapon_interface", // The translation key of the keybinding's name
                InputConstants.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_Y, // The keycode of the key
                "key.enhancedarmaments" // The translation key of the keybinding's category.
        ));
        InputEventHandler.onKeyPress();
    }
}
