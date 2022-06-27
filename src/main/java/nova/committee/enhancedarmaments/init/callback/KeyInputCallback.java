package nova.committee.enhancedarmaments.init.callback;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/6/28 0:56
 * Version: 1.0
 */
@Environment(EnvType.CLIENT)
public interface KeyInputCallback {
    Event<KeyInputCallback> EVENT = EventFactory.createArrayBacked(KeyInputCallback.class, callbacks -> (key, scancode, action, mods) -> {
        for (KeyInputCallback callback : callbacks) {
            callback.onKeyInput(key, scancode, action, mods);
        }
    });

    void onKeyInput(int key, int scancode, int action, int mods);
}
