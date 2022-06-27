package nova.committee.enhancedarmaments.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import nova.committee.enhancedarmaments.core.Rarity;
import nova.committee.enhancedarmaments.util.EAUtil;
import nova.committee.enhancedarmaments.util.NBTUtil;

public class RarityCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("changerarity")
                .requires(cmd -> cmd.hasPermission(3))
                .then(Commands.argument("rarityid", IntegerArgumentType.integer()))
                .executes(cmd -> changeRarity(cmd.getSource(), cmd.getSource().getPlayerOrException(), IntegerArgumentType.getInteger(cmd, "rarityid"))));
    }

    public static int changeRarity(CommandSourceStack src, Player player, int rarityid) {
        if ((rarityid < 1) || (rarityid > 6))
            src.sendSuccess(new TextComponent("Rarity ID must be 1, 2, 3, 4, 5 or 6!"), true);
        else {
            if (!EAUtil.canEnhance(player.getMainHandItem().getItem()))
                src.sendSuccess(new TextComponent("Hold a weapon or an armor in your mainhand!"), true);
            else {
                ItemStack item = player.getMainHandItem();
                CompoundTag nbt = NBTUtil.loadStackNBT(item);
                Rarity.setRarity(nbt, String.valueOf(rarityid));
                NBTUtil.saveStackNBT(item, nbt);
                player.setItemInHand(InteractionHand.MAIN_HAND, item);
                src.sendSuccess(new TranslatableComponent("enhancedarmaments.command.success"), true);
            }
        }
        return 0;
    }
}
