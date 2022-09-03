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
import nova.committee.enhancedarmaments.core.Rarity;
import nova.committee.enhancedarmaments.util.EAUtil;
import nova.committee.enhancedarmaments.util.NBTUtil;

public class RarityCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("changerarity")
                .requires(cmd -> cmd.hasPermission(2))
                .then(Commands.argument("rarityid", IntegerArgumentType.integer())
                        .executes(cmd -> changeRarity(cmd.getSource(), cmd.getSource().getPlayerOrException(), cmd.getArgument("rarityid", Integer.class))))
        );
    }

    public static int changeRarity(CommandSourceStack src, Player player, int rarityid) {
        if ((rarityid < 1) || (rarityid > 6))
            src.sendSuccess(Component.translatable("enhancedarmaments.misc.info.rarity_bigger"), true);
        else {
            if (!EAUtil.canEnhance(player.getMainHandItem().getItem()))
                src.sendSuccess(Component.translatable("enhancedarmaments.misc.info.mainhand"), true);
            else {
                ItemStack item = player.getMainHandItem();
                CompoundTag nbt = NBTUtil.loadStackNBT(item);
                Rarity.setRarity(nbt, String.valueOf(rarityid));
                NBTUtil.saveStackNBT(item, nbt);
                player.setItemInHand(InteractionHand.MAIN_HAND, item);
                src.sendSuccess(Component.translatable("enhancedarmaments.command.success"), true);
            }
        }
        return 0;
    }
}
