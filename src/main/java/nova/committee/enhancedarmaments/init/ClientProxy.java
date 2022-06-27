package nova.committee.enhancedarmaments.init;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy implements IProxy {
    public KeyBinding abilityKey;

    @Override
    public void init() {
        abilityKey = new KeyBinding("key.gui.weapon_interface", 75, "key.enhancedarmaments");
        ClientRegistry.registerKeyBinding(abilityKey);
    }
}
