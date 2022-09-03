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
import net.minecraft.util.text.TranslationTextComponent;
import nova.committee.enhancedarmaments.EnhancedArmaments;
import nova.committee.enhancedarmaments.core.Rarity;
import nova.committee.enhancedarmaments.util.EAUtil;
import nova.committee.enhancedarmaments.util.NBTUtil;

public class RarityCommand {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(
                Commands.literal("changerarity")
                .requires(cmd -> cmd.hasPermission(2))
                .then(
                        Commands.argument("rarityid", IntegerArgumentType.integer())
                                .executes(RarityCommand::changeRarity))

                );
    }

    public static int changeRarity(CommandContext<CommandSource> cmd) throws CommandSyntaxException {
        PlayerEntity player = cmd.getSource().getPlayerOrException();
        int rarityid = IntegerArgumentType.getInteger(cmd, "rarityid");
        EnhancedArmaments.LOGGER.info(rarityid);
        System.out.println(rarityid);
        if ((rarityid < 1) || (rarityid > 6))
            cmd.getSource().sendSuccess(new TranslationTextComponent("enhancedarmaments.misc.info.rarity_bigger"), true);
        else {
            if (!EAUtil.canEnhance(player.getMainHandItem().getItem()))
                cmd.getSource().sendSuccess(new TranslationTextComponent("enhancedarmaments.misc.info.mainhand"), true);
            else {
                ItemStack item = player.getMainHandItem();
                CompoundNBT nbt = NBTUtil.loadStackNBT(item);
                Rarity.setRarity(nbt, rarityid);
                NBTUtil.saveStackNBT(item, nbt);
                player.setItemInHand(Hand.MAIN_HAND, item);
                cmd.getSource().sendSuccess(new TranslationTextComponent("enhancedarmaments.command.success"), true);
            }
        }
        return 0;
    }
}
