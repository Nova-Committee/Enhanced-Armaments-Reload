package nova.committee.enhancedarmaments.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import nova.committee.enhancedarmaments.EnhancedArmaments;
import nova.committee.enhancedarmaments.core.Experience;
import nova.committee.enhancedarmaments.util.EAUtil;
import nova.committee.enhancedarmaments.util.NBTUtil;

public class AddLevelCommand {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("addlevel")
                .requires(cmd -> cmd.hasPermission(2))
                .then(
                        Commands.argument("level", IntegerArgumentType.integer())
                                .executes(AddLevelCommand::addLevel))

                );
    }

    private static int addLevel(CommandContext<CommandSource> cmd) throws CommandSyntaxException {
        PlayerEntity player = cmd.getSource().getPlayerOrException();
        int count = cmd.getArgument("level", Integer.class);
        EnhancedArmaments.LOGGER.info(count);
        System.out.print(count);
        if (count < 1) cmd.getSource().sendSuccess(new StringTextComponent("enhancedarmaments.misc.info.level_bigger"), true);
        else {
            if (!EAUtil.canEnhance(player.getMainHandItem().getItem()))
                cmd.getSource().sendSuccess(new StringTextComponent("enhancedarmaments.misc.info.mainhand"), true);
            else {
                ItemStack item = player.getMainHandItem();
                CompoundNBT nbt = NBTUtil.loadStackNBT(item);
                for (int i = 0; i < count; i++) {
                    if (Experience.canLevelUp(nbt)) {
                        Experience.setExperience(nbt, Experience.getExperience(nbt) + Experience.getNeededExpForNextLevel(nbt));
                        Experience.setLevel(nbt, Experience.getLevel(nbt) + 1);
                        Experience.setAbilityTokens(nbt, Experience.getAbilityTokens(nbt) + 1);
                    }
                }
                NBTUtil.saveStackNBT(item, nbt);
                player.setItemInHand(Hand.MAIN_HAND, item);
                cmd.getSource().sendSuccess(new TranslationTextComponent("enhancedarmaments.command.success"), true);
            }
        }
        return 1;
    }
}