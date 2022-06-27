package nova.committee.enhancedarmaments.common.network;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/1/20 20:59
 * Version: 1.0
 */
public abstract class IPacket {

    public abstract void toBytes(PacketBuffer buffer);

    public abstract void handle(Supplier<NetworkEvent.Context> ctx);

}
