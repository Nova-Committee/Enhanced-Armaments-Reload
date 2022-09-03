package nova.committee.enhancedarmaments.init.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nova.committee.enhancedarmaments.EnhancedArmaments;
import nova.committee.enhancedarmaments.client.gui.AbilitySelectionGui;
import nova.committee.enhancedarmaments.init.ClientProxy;
import nova.committee.enhancedarmaments.util.EAUtil;


/**
 * Opens the weapon ability selection gui on key press.
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class InputEventHandler {
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onKeyPress(InputEvent.KeyInputEvent event) {
        KeyBinding key = ((ClientProxy) EnhancedArmaments.proxy).abilityKey;
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;

        if (player != null) {
            ItemStack stack = player.inventory.getSelected();

            if (stack != ItemStack.EMPTY) {
                if (EAUtil.canEnhance(stack.getItem())) {
                    if (key.isDown() && stack.hasTag())
                        if (stack.getOrCreateTag().contains("EA_ENABLED"))
                            mc.setScreen(new AbilitySelectionGui());
                }
            }
        }
    }
}
