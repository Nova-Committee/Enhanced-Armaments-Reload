package nova.committee.enhancedarmaments.client.widgets;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.resources.ResourceLocation;
import nova.committee.enhancedarmaments.util.ScreenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/3/25 18:42
 * Version: 1.0
 */
public class TypeTab {
    private final List<SkillButton> skillButtons;
    private final int marketButtonsX;
    private final int marketButtonsY;
    private final ScreenRect rect;
    private final String name;

    public TypeTab(int tabX, int tabY, String name, int marketButtonsX, int marketButtonsY) {
        skillButtons = new ArrayList<>();
        this.rect = new ScreenRect(tabX, tabY, 48, 12);
        this.name = name;
        this.marketButtonsX = marketButtonsX;
        this.marketButtonsY = marketButtonsY;
    }

    public SkillButton addButton(int index, Button.OnPress pressable) {

        if (index <= 20) {
            SkillButton button = new SkillButton(index, 0, 0, new ResourceLocation(""), pressable);
            button.active = false;
            skillButtons.add(button);
            return button;
        }

        return null;
    }

    public SkillButton getButton(int index) {
        return skillButtons.get(index);
    }

    public ScreenRect getRect() {
        return rect;
    }

    public void updateButtons() {

        int count = 0;

        for (SkillButton button : skillButtons) {

            if (button.active) {

                int rowSize = 10;

                int xPos = (marketButtonsX) + ((count % rowSize) * 18);
                int yPos = (marketButtonsY) + ((count / rowSize) * 18);
                int size = 16;

                button.setRect(new ScreenRect(xPos, yPos, size, size));
                count++;
            }
        }
    }

    public void enableButtons(boolean value) {

        for (SkillButton button : skillButtons) {
            button.active = value;
        }
    }

    public void renderTab(GuiGraphics graphics) {
        ScreenHelper.drawCenteredString(graphics, name, rect.x + rect.width / 2, rect.y, 0, 0xFFFFFF);
    }

    public void renderSelectedTab(GuiGraphics graphics) {
        ScreenHelper.bindGuiTextures();
        ScreenHelper.drawRect(graphics, rect.x, rect.y + 9, 0, 0, 100, rect.width - 1, 1);
    }

}
