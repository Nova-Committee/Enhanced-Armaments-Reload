package nova.committee.enhancedarmaments.init.handler;

import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nova.committee.enhancedarmaments.common.commands.AddLevelCommand;
import nova.committee.enhancedarmaments.common.commands.RarityCommand;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/3/25 14:19
 * Version: 1.0
 */

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommandEventHandler {

    @SubscribeEvent
    public static void registry(RegisterCommandsEvent event) {
        AddLevelCommand.register(event.getDispatcher());
        RarityCommand.register(event.getDispatcher());
    }
}
