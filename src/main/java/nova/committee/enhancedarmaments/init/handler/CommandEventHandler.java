package nova.committee.enhancedarmaments.init.handler;

import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import nova.committee.enhancedarmaments.common.commands.AddLevelCommand;
import nova.committee.enhancedarmaments.common.commands.RarityCommand;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/3/25 14:19
 * Version: 1.0
 */
public class CommandEventHandler {


    public static void init() {
        CommandRegistrationCallback.EVENT.register((dispatcher, e) -> {
            RarityCommand.register(dispatcher);
            AddLevelCommand.register(dispatcher);
        });
    }
}
