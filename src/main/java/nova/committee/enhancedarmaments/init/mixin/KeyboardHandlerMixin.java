package nova.committee.enhancedarmaments.init.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.KeyboardHandler;
import nova.committee.enhancedarmaments.init.callback.KeyInputCallback;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/6/28 0:57
 * Version: 1.0
 */
@Environment(EnvType.CLIENT)
@Mixin(KeyboardHandler.class)
public abstract class KeyboardHandlerMixin {
    // First return opcode is jumped over if condition is met.
    @Inject(
            method = "keyPress",
            slice = @Slice(
                    from = @At(
                            value = "RETURN",
                            ordinal = 0,
                            shift = At.Shift.AFTER
                    )
            ),
            at = @At(value = "RETURN")
    )
    public void port_lib$onHandleKeyInput(long window, int key, int scancode, int action, int mods, CallbackInfo ci) {
        KeyInputCallback.EVENT.invoker().onKeyInput(key, scancode, action, mods);
    }
}
