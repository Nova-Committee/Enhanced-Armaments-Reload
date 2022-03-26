package nova.committee.enhancedarmaments.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.entity.ItemRenderer;

import java.util.List;

import static net.minecraft.client.gui.GuiComponent.fill;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/3/25 13:41
 * Version: 1.0
 */
public class ScreenUtil {
    protected static Minecraft minecraft = Minecraft.getInstance();
    protected static Font font;
    protected ItemRenderer itemRenderer;

    {
        minecraft = Minecraft.getInstance();
        itemRenderer = minecraft.getItemRenderer();
        font = minecraft.font;
    }

    public static void drawRectWithBackground(PoseStack matrixStack, int x, int y, int height, int width, int frameColor, int backColor) {
        fill(matrixStack, x - 1, y - 1, x + width + 1, y, frameColor);
        fill(matrixStack, x - 1, y + height, x + width + 1, y + height + 1, frameColor);
        fill(matrixStack, x - 1, y, x, y + height, frameColor);
        fill(matrixStack, x + width, y, x + width + 1, y + height, frameColor);
        fill(matrixStack, x, y, x + width, y + height, backColor);
    }

    public static void drawHoverTooltip(PoseStack matrixStack, List<String> strings, int x, int y) {
        int maxLength = 0;
        for (String s : strings) {
            maxLength = Math.max(minecraft.font.width(s), maxLength);
        }
        drawRectWithBackground(matrixStack, x, y, strings.size() * 9 + 3, maxLength + 4, 0x80ffffff, 0xc0000000);
        for (int i = 0; i < strings.size(); i++) {
            minecraft.font.draw(matrixStack, strings.get(i), x + 2, y + 2 + 9 * i, 0xffffff);
        }
    }


}
