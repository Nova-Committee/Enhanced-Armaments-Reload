package nova.committee.enhancedarmaments.init;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class KeyInit {
    public static final KeyMapping abilityKey = new KeyMapping("key.gui.weapon_interface", GLFW.GLFW_KEY_K, "key.enhancedarmaments");

    @SubscribeEvent
    public static void onKeyRegistry(RegisterKeyMappingsEvent event) {
        event.register(abilityKey);
    }
}
