package nova.committee.enhancedarmaments.client.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import nova.committee.enhancedarmaments.util.ScreenHelper;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/3/25 18:44
 * Version: 1.0
 */
public class SkillButton extends Button {
    private final int marketListIndex;
    protected ScreenRect rect;
    private ResourceLocation res;
    private String[] tooltip;

    public SkillButton(int marketListIndex, int x, int y, ResourceLocation img, OnPress press) {
        super(x, y, 16, 16, Component.literal(""), press, Button.DEFAULT_NARRATION);

        this.rect = new ScreenRect(x, y, width, height);
        this.res = img;
        this.marketListIndex = marketListIndex;
    }

    public int getMarketListIndex() {
        return marketListIndex;
    }


    public void setRect(ScreenRect rect) {
        this.rect = rect;
        this.setX(rect.x);
        this.setY(rect.y);
        this.width = rect.width;
        this.height = rect.height;
    }

    public void renderSelectionBox(GuiGraphics graphics) {
        if (this.visible && this.active) {
            ScreenHelper.bindGuiTextures();
            ScreenHelper.drawRect(graphics, rect.x - 1, rect.y - 1, 0, 91, 0, 19, 19);
        }
    }

    public void setTooltip(String... tooltip) {
        this.tooltip = tooltip;
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {

        isHovered = rect.contains(mouseX, mouseY);

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, this.res);

        RenderSystem.enableDepthTest();

        graphics.blit(this.res, this.getX(), this.getY(), 0,0, 16, 16);

        if (this.visible && this.active) {
            ScreenHelper.drawHoveringTextBox(graphics, mouseX, mouseY, 150, rect, tooltip);
        }

    }
}
