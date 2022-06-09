package nova.committee.enhancedarmaments.client.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
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
        super(x, y, 16, 16, Component.literal(""), press);
        this.rect = new ScreenRect(this.x, this.y, width, height);
        this.res = img;
        this.marketListIndex = marketListIndex;
    }

    public int getMarketListIndex() {
        return marketListIndex;
    }


    public void setRect(ScreenRect rect) {
        this.rect = rect;
        this.x = rect.x;
        this.y = rect.y;
        this.width = rect.width;
        this.height = rect.height;
    }

    public void renderSelectionBox(PoseStack poseStack) {
        if (this.visible && this.active) {
            ScreenHelper.bindGuiTextures();
            ScreenHelper.drawRect(poseStack, rect.x - 1, rect.y - 1, 0, 91, 0, 19, 19);
        }
    }

    public void setTooltip(String... tooltip) {
        this.tooltip = tooltip;
    }

    @Override
    public void renderButton(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {

        isHovered = rect.contains(mouseX, mouseY);

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, this.res);

        RenderSystem.enableDepthTest();
        blit(matrixStack, x, y, 0, 0, 16, 16);

        if (this.visible && this.active) {
            ScreenHelper.drawHoveringTextBox(matrixStack, mouseX, mouseY, 150, rect, tooltip);
        }

    }
}
