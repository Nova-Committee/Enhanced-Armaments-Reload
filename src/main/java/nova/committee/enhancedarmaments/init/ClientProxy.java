package nova.committee.enhancedarmaments.init;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.ClientRegistry;

public class ClientProxy implements ISidedProxy {
    public KeyMapping abilityKey;

    @Override
    public void init() {
        abilityKey = new KeyMapping("key.gui.weapon_interface", 75, "key.enhancedarmaments");
        ClientRegistry.registerKeyBinding(abilityKey);
    }
}
