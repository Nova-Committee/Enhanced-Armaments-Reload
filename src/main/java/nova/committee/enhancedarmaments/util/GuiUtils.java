package nova.committee.enhancedarmaments.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/6/28 1:02
 * Version: 1.0
 */
public class GuiUtils {
    public static void drawContinuousTexturedBox(
            PoseStack poseStack, ResourceLocation res, int x, int y, int u, int v, int width, int height, int textureWidth, int textureHeight,
            int topBorder, int bottomBorder, int leftBorder, int rightBorder, float zLevel) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, res);
        drawContinuousTexturedBox(poseStack, x, y, u, v, width, height, textureWidth, textureHeight, topBorder, bottomBorder, leftBorder, rightBorder, zLevel);
    }

    /**
     * Draws a textured box of any size (smallest size is borderSize * 2 square) based on a fixed size textured box with continuous borders
     * and filler. It is assumed that the desired texture ResourceLocation object has been bound using
     * Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation).
     *
     * @param poseStack the gui pose stack
     * @param x x axis offset
     * @param y y axis offset
     * @param u bound resource location image x offset
     * @param v bound resource location image y offset
     * @param width the desired box width
     * @param height the desired box height
     * @param textureWidth the width of the box texture in the resource location image
     * @param textureHeight the height of the box texture in the resource location image
     * @param topBorder the size of the box's top border
     * @param bottomBorder the size of the box's bottom border
     * @param leftBorder the size of the box's left border
     * @param rightBorder the size of the box's right border
     * @param zLevel the zLevel to draw at
     */
    public static void drawContinuousTexturedBox(
            PoseStack poseStack, int x, int y, int u, int v, int width, int height, int textureWidth, int textureHeight,
            int topBorder, int bottomBorder, int leftBorder, int rightBorder, float zLevel) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        int fillerWidth = textureWidth - leftBorder - rightBorder;
        int fillerHeight = textureHeight - topBorder - bottomBorder;
        int canvasWidth = width - leftBorder - rightBorder;
        int canvasHeight = height - topBorder - bottomBorder;
        int xPasses = canvasWidth / fillerWidth;
        int remainderWidth = canvasWidth % fillerWidth;
        int yPasses = canvasHeight / fillerHeight;
        int remainderHeight = canvasHeight % fillerHeight;

        // Draw Border
        // Top Left
        drawTexturedModalRect(poseStack, x, y, u, v, leftBorder, topBorder, zLevel);
        // Top Right
        drawTexturedModalRect(poseStack, x + leftBorder + canvasWidth, y, u + leftBorder + fillerWidth, v, rightBorder, topBorder, zLevel);
        // Bottom Left
        drawTexturedModalRect(poseStack, x, y + topBorder + canvasHeight, u, v + topBorder + fillerHeight, leftBorder, bottomBorder, zLevel);
        // Bottom Right
        drawTexturedModalRect(poseStack, x + leftBorder + canvasWidth, y + topBorder + canvasHeight, u + leftBorder + fillerWidth, v + topBorder + fillerHeight, rightBorder, bottomBorder, zLevel);

        for (int i = 0; i < xPasses + (remainderWidth > 0 ? 1 : 0); i++)
        {
            // Top Border
            drawTexturedModalRect(poseStack, x + leftBorder + (i * fillerWidth), y, u + leftBorder, v, (i == xPasses ? remainderWidth : fillerWidth), topBorder, zLevel);
            // Bottom Border
            drawTexturedModalRect(poseStack, x + leftBorder + (i * fillerWidth), y + topBorder + canvasHeight, u + leftBorder, v + topBorder + fillerHeight, (i == xPasses ? remainderWidth : fillerWidth), bottomBorder, zLevel);

            // Throw in some filler for good measure
            for (int j = 0; j < yPasses + (remainderHeight > 0 ? 1 : 0); j++)
                drawTexturedModalRect(poseStack, x + leftBorder + (i * fillerWidth), y + topBorder + (j * fillerHeight), u + leftBorder, v + topBorder, (i == xPasses ? remainderWidth : fillerWidth), (j == yPasses ? remainderHeight : fillerHeight), zLevel);
        }

        // Side Borders
        for (int j = 0; j < yPasses + (remainderHeight > 0 ? 1 : 0); j++)
        {
            // Left Border
            drawTexturedModalRect(poseStack, x, y + topBorder + (j * fillerHeight), u, v + topBorder, leftBorder, (j == yPasses ? remainderHeight : fillerHeight), zLevel);
            // Right Border
            drawTexturedModalRect(poseStack, x + leftBorder + canvasWidth, y + topBorder + (j * fillerHeight), u + leftBorder + fillerWidth, v + topBorder, rightBorder, (j == yPasses ? remainderHeight : fillerHeight), zLevel);
        }
    }

    public static void drawTexturedModalRect(PoseStack poseStack, int x, int y, int u, int v, int width, int height, float zLevel)
    {
        final float uScale = 1f / 0x100;
        final float vScale = 1f / 0x100;

        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder wr = tessellator.getBuilder();
        wr.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        Matrix4f matrix = poseStack.last().pose();
        wr.vertex(matrix, x        , y + height, zLevel).uv( u          * uScale, ((v + height) * vScale)).endVertex();
        wr.vertex(matrix, x + width, y + height, zLevel).uv((u + width) * uScale, ((v + height) * vScale)).endVertex();
        wr.vertex(matrix, x + width, y         , zLevel).uv((u + width) * uScale, ( v           * vScale)).endVertex();
        wr.vertex(matrix, x        , y         , zLevel).uv( u          * uScale, ( v           * vScale)).endVertex();
        tessellator.end();
    }
}
