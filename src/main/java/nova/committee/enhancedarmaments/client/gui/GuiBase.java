package nova.committee.enhancedarmaments.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import nova.committee.enhancedarmaments.util.ScreenHelper;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/3/25 18:20
 * Version: 1.0
 */
public abstract class GuiBase extends Screen {

    protected GuiBase() {

        super(Component.literal(""));

    }

    /**
     * Used to obtain the GUI's texture so it can render it.
     */
    protected abstract String getGuiTextureName();

    /**
     * Used to render anything in the background layer.
     */
    protected abstract void drawGuiBackground(PoseStack matrixStack, int mouseX, int mouseY);

    /**
     * Used to render anything in the foreground layer.
     */
    protected abstract void drawGuiForeground(PoseStack matrixStack, int mouseX, int mouseY);

    /**
     * Used to determine the width of the GUI.
     */
    protected abstract int getGuiSizeX();

    /**
     * Used to determine the height of the GUI.
     */
    protected abstract int getGuiSizeY();

    /**
     * Used to determine the left of the GUI.
     */
    protected int getScreenX() {
        return (this.width - getGuiSizeX()) / 2;
    }

    /**
     * Used to determine the top of the GUI.
     */
    protected int getScreenY() {
        return (this.height - getGuiSizeY()) / 2;
    }

    protected abstract boolean canCloseWithInvKey();

    /**
     * The base render method. Handles ALL rendering.
     */
    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float f1) {

        renderBackground(matrixStack);

        if (getGuiTextureName() != null) {
            ScreenHelper.bindTexture(getGuiTextureName());
            ScreenHelper.drawRect(matrixStack, getScreenX(), getScreenY(), 0, 0, 0, getGuiSizeX(), getGuiSizeY());
        }

        drawGuiBackground(matrixStack, mouseX, mouseY);
        super.render(matrixStack, mouseX, mouseY, f1);
        drawGuiForeground(matrixStack, mouseX, mouseY);
    }


}
