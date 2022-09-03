package nova.committee.enhancedarmaments.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import nova.committee.enhancedarmaments.EnhancedArmaments;
import nova.committee.enhancedarmaments.Static;
import nova.committee.enhancedarmaments.client.widgets.ScreenRect;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class ScreenHelper {

    private static final int TEXTURE_SIZE = 256;
    private static final Minecraft mc = Minecraft.getInstance();

    public static void bindGuiTextures() {
        mc.getTextureManager().bindForSetup(new ResourceLocation(Static.MODID + ":textures/gui/gui_textures.png"));
    }

    public static void bindTexture(String name) {
        mc.getTextureManager().bindForSetup(new ResourceLocation(Static.MODID + ":textures/gui/" + name + ".png"));
    }

    public static void drawHoveringTextBox(PoseStack PoseStack, int mouseX, int mouseY, int zLevel, ScreenRect rect, String... text) {

        if (rect.contains(mouseX, mouseY)) {
            drawTextBox(PoseStack, mouseX + 8, mouseY - 10, zLevel, false, text);
        }
    }

    public static void drawTextBox(PoseStack poseStack, int x, int y, int zLevel, boolean centeredString, String... text) {

        poseStack.pushPose();
        poseStack.translate(0, 0, zLevel);

        List<String> textToRender = new ArrayList<>();

        int maxLength = mc.font.width(text[0]);

        for (String str : text) {

            if (str.length() >= 80) {

                textToRender.add(str.substring(0, str.length() / 2));
                textToRender.add(StringHelper.addAllFormats(StringHelper.getFormatsFromString(str)) + str.substring(str.length() / 2));
            } else textToRender.add(str);
        }

        for (String str : textToRender) {

            if (mc.font.width(str) > maxLength) {
                maxLength = mc.font.width(str);
            }
        }

        bindTexture("tooltip");
        drawCappedRect(poseStack, x + (centeredString ? -maxLength / 2 : 0), y, 0, 0, 0, maxLength + 5, 13 + ((textToRender.size() - 1) * 9), 512, 512);

        poseStack.translate(0, 0, 100);
        for (int i = 0; i < textToRender.size(); i++) {

            String str = textToRender.get(i);
            mc.font.draw(poseStack, ChatFormatting.WHITE + str, x + 3 + (centeredString ? -(int) (mc.font.width(str) / 2) : 0), y + 3 + (i * 9), 0xFFFFFF);
        }

        poseStack.translate(0, 0, 0);
        poseStack.clear();
        poseStack.popPose();
    }

    public static void drawCappedRect(PoseStack PoseStack, int x, int y, int u, int v, int zLevel, int width, int height, int maxWidth, int maxHeight) {

        //TOP LEFT
        drawRect(PoseStack, x, y, u, v, zLevel, Math.min(width - 2, maxWidth), Math.min(height - 2, maxHeight));

        //RIGHT
        if (width <= maxWidth)
            drawRect(PoseStack, x + width - 2, y, u + maxWidth - 2, v, zLevel, 2, Math.min(height - 2, maxHeight));

        //BOTTOM
        if (height <= maxHeight)
            drawRect(PoseStack, x, y + height - 2, u, v + maxHeight - 2, zLevel, Math.min(width - 2, maxWidth), 2);

        //BOTTOM RIGHT
        if (width <= maxWidth && height <= maxHeight)
            drawRect(PoseStack, x + width - 2, y + height - 2, u + maxWidth - 2, v + maxHeight - 2, zLevel, 2, 2);
    }

    public static void drawRect(PoseStack PoseStack, int x, int y, int u, int v, int zLevel, int width, int height) {

        PoseStack.pushPose();
        PoseStack.translate(0, 0, zLevel);


        int maxX = x + width;
        int maxY = y + height;

        int maxU = u + width;
        int maxV = v + height;

        float pixel = 1F / TEXTURE_SIZE;

        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.getBuilder();

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        buffer.vertex((float) x, (float) maxY, 50).uv(u * pixel, maxV * pixel).endVertex();
        buffer.vertex((float) maxX, (float) maxY, 50).uv(maxU * pixel, maxV * pixel).endVertex();
        buffer.vertex((float) maxX, (float) y, 50).uv(maxU * pixel, v * pixel).endVertex();
        buffer.vertex((float) x, (float) y, 50).uv(u * pixel, v * pixel).endVertex();
        tessellator.end();

        RenderSystem.disableBlend();

        PoseStack.popPose();
    }

    public static void drawLimitedString(PoseStack PoseStack, String text, int x, int y, int textLimit, int color) {

        String temp = text;

        if (temp.length() > textLimit) {

            temp = temp.substring(0, textLimit - 1);
            temp += "...";
        }

        mc.font.draw(PoseStack, temp, x, y, color);
        GL11.glColor4f(1, 1, 1, 1);
    }

    public static void drawCenteredString(PoseStack poseStack, String text, int x, int y, int zLevel, int color) {

        poseStack.pushPose();
        poseStack.translate(0, 0, 50 + zLevel);
        mc.font.draw(poseStack, text, x - (float) (mc.font.width(text) / 2), y, color);
        poseStack.popPose();
    }

    public static void drawColoredRect(int x, int y, int zLevel, int width, int height, int hex, float alpha) {

        float r = (hex >> 16) & 0xFF;
        float g = (hex >> 8) & 0xFF;
        float b = (hex) & 0xFF;

        float red = ((((r * 100) / 255) / 100));
        float green = ((((g * 100) / 255) / 100));
        float blue = ((((b * 100) / 255) / 100));

        int maxX = x + width;
        int maxY = y + height;

        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.getBuilder();

        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        buffer.vertex(x, maxY, zLevel).color(red, green, blue, alpha).endVertex();
        buffer.vertex(maxX, maxY, zLevel).color(red, green, blue, alpha).endVertex();
        buffer.vertex(maxX, y, zLevel).color(red, green, blue, alpha).endVertex();
        buffer.vertex(x, y, zLevel).color(red, green, blue, alpha).endVertex();
        tessellator.end();

        RenderSystem.disableBlend();
        RenderSystem.enableTexture();
    }

    public static void drawItemStack(ItemRenderer itemRender, ItemStack stack, int x, int y) {

        //RenderHelper.func_227780_a_();
        GL11.glTranslatef(0.0F, 0.0F, 0.0F);
        itemRender.blitOffset = -100;
        itemRender.renderGuiItem(stack, x, y);
        itemRender.blitOffset = 0F;
    }
}
