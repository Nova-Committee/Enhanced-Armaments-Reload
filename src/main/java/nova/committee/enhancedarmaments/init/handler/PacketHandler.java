package nova.committee.enhancedarmaments.init.handler;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import nova.committee.enhancedarmaments.EnhancedArmaments;
import nova.committee.enhancedarmaments.common.network.GuiAbilityPacket;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/1/20 20:41
 * Version: 1.0
 */
public class PacketHandler {
    public static final String VERSION = "1.0";
    public static SimpleChannel INSTANCE;
    private static int ID = 0;

    public static int nextID() {
        return ID++;
    }

    public static void registerMessage() {
        INSTANCE = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(EnhancedArmaments.MODID, "network"),
                () -> VERSION,
                (version) -> version.equals(VERSION),
                (version) -> version.equals(VERSION)
        );
        INSTANCE.registerMessage(nextID(), GuiAbilityPacket.class, GuiAbilityPacket::toBytes, GuiAbilityPacket::new, GuiAbilityPacket::handle);


    }

}
