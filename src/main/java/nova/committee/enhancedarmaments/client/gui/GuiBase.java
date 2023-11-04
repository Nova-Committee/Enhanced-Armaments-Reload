package nova.committee.enhancedarmaments.client.gui;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.GuiGraphics;
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
    protected abstract void drawGuiBackground(GuiGraphics graphics, int mouseX, int mouseY);

    /**
     * Used to render anything in the foreground layer.
     */
    protected abstract void drawGuiForeground(GuiGraphics graphics, int mouseX, int mouseY);

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
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float f1) {

        renderBackground(graphics);
        
        if (getGuiTextureName() != null) {
            ScreenHelper.bindTexture(getGuiTextureName());
            ScreenHelper.drawRect(graphics, getScreenX(), getScreenY(), 0, 0, 0, getGuiSizeX(), getGuiSizeY());
        }
        
        drawGuiBackground(graphics, mouseX, mouseY);
        super.render(graphics, mouseX, mouseY, f1);
        drawGuiForeground(graphics, mouseX, mouseY);
    }


}
