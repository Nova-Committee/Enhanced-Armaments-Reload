package nova.committee.enhancedarmaments.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import nova.committee.enhancedarmaments.core.Experience;
import nova.committee.enhancedarmaments.util.EAUtil;
import nova.committee.enhancedarmaments.util.NBTUtil;

public class AddLevelCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("addlevel")
                .requires(cmd -> cmd.hasPermission(2))
                .then(Commands.argument("level", IntegerArgumentType.integer())
                        .executes(cmd -> addLevel(cmd.getSource(), cmd.getSource().getPlayerOrException(), cmd.getArgument("level", Integer.class))))
        );
    }

    private static int addLevel(CommandSourceStack cmd, Player player, int count) {
        if (count < 1) cmd.sendSuccess(Component.translatable("enhancedarmaments.misc.info.level_bigger"), true);
        else {
            if (!EAUtil.canEnhance(player.getMainHandItem().getItem()))
                cmd.sendSuccess(Component.translatable("enhancedarmaments.misc.info.mainhand"), true);
            else {
                ItemStack item = player.getMainHandItem();
                CompoundTag nbt = NBTUtil.loadStackNBT(item);
                for (int i = 0; i < count; i++) {
                    if (Experience.canLevelUp(nbt)) {
                        Experience.setExperience(nbt, Experience.getExperience(nbt) + Experience.getNeededExpForNextLevel(nbt));
                        Experience.setLevel(nbt, Experience.getLevel(nbt) + 1);
                        Experience.setAbilityTokens(nbt, Experience.getAbilityTokens(nbt) + 1);
                    }
                }
                NBTUtil.saveStackNBT(item, nbt);
                player.setItemInHand(InteractionHand.MAIN_HAND, item);
                cmd.sendSuccess(Component.translatable("enhancedarmaments.command.success"), true);
            }
        }
        return 0;
    }
}
