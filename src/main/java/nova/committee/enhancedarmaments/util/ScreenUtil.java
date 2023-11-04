package nova.committee.enhancedarmaments.util;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.entity.ItemRenderer;
import java.util.List;

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

    public static void drawRectWithBackground(GuiGraphics graphics, int x, int y, int height, int width, int frameColor, int backColor) {
        graphics.fill( x - 1, y - 1, x + width + 1, y, frameColor);
        graphics.fill( x - 1, y + height, x + width + 1, y + height + 1, frameColor);
        graphics.fill( x - 1, y, x, y + height, frameColor);
        graphics.fill( x + width, y, x + width + 1, y + height, frameColor);
        graphics.fill( x, y, x + width, y + height, backColor);
    }

    public static void drawHoverTooltip(GuiGraphics graphics, List<String> strings, int x, int y) {
        int maxLength = 0;
        for (String s : strings) {
            maxLength = Math.max(minecraft.font.width(s), maxLength);
        }
        drawRectWithBackground(graphics, x, y, strings.size() * 9 + 3, maxLength + 4, 0x80ffffff, 0xc0000000);
        for (int i = 0; i < strings.size(); i++) {
            graphics.drawWordWrap(font, null, i, x, y, maxLength);
        }
    }


}
