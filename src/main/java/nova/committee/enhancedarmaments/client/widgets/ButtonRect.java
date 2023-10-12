package nova.committee.enhancedarmaments.client.widgets;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nova.committee.enhancedarmaments.util.ScreenHelper;

@OnlyIn(Dist.CLIENT)
public class ButtonRect extends Button {

    public final ScreenRect rect;

    /**
     * 用作mod中任何东西的基本按钮
     *
     * @param text      按钮上的文字
     * @param pressable 按下时的动作
     */
    public ButtonRect(int x, int y, int width, String text, OnPress pressable) {
        super(width, 16, x, y, Component.literal(text), pressable, Button.DEFAULT_NARRATION);
        this.setX(x);
        this.setY(y);
        this.width = width;
        this.height = 16;

        rect = new ScreenRect(x, y, width, 16);
    }

    public void setPosition(int x, int y) {
        rect.x = x;
        rect.y = y;
        this.setX(x);
        this.setY(y);
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {

        if (visible) {

            if (rect.contains(mouseX, mouseY)) {
                RenderSystem.clearColor(1F, 1F, 1F, 1F);
                isHovered = true;
            } else {
                RenderSystem.clearColor(0.8F, 0.8F, 0.8F, 8F);
                isHovered = false;
            }

            if (!active) {
                RenderSystem.clearColor(0.5F, 0.5F, 0.5F, 1F);
            }

            ScreenHelper.bindGuiTextures();
            ScreenHelper.drawCappedRect(graphics , rect.x, rect.y, 0, 240, 5, rect.width, rect.height, 256, 16);
            
            RenderSystem.clearColor(1, 1, 1, 1);

            ScreenHelper.drawCenteredString(graphics, getMessage().getString(), rect.x + (rect.width / 2), rect.y + (rect.height - 8) / 2, 100, 0xFFFFFF);
        }
    }
}
