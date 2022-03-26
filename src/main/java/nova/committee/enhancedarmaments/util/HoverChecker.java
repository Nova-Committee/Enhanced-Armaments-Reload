package nova.committee.enhancedarmaments.util;

import net.minecraft.client.gui.components.AbstractWidget;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/3/25 16:48
 * Version: 1.0
 */
public class HoverChecker {
    private int top, bottom, left, right, threshold;
    private AbstractWidget widget;
    private long hoverStart;

    public HoverChecker(int top, int bottom, int left, int right, int threshold) {
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;
        this.threshold = threshold;
        this.hoverStart = -1;
    }

    public HoverChecker(AbstractWidget widget, int threshold) {
        this.widget = widget;
        this.threshold = threshold;
    }

    /**
     * Call this method if the intended region has changed such as if the region must follow a scrolling list.
     * It is not necessary to call this method if a GuiButton defines the hover region.
     */
    public void updateBounds(int top, int bottom, int left, int right) {
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;
    }

    /**
     * Checks if the mouse is in the hover region. If the specified time period has elapsed the method returns true.
     * The hover timer is reset if the mouse is not within the region.
     */
    public boolean checkHover(int mouseX, int mouseY) {
        return checkHover(mouseX, mouseY, true);
    }

    /**
     * Checks if the mouse is in the hover region. If the specified time period has elapsed the method returns true.
     * The hover timer is reset if the mouse is not within the region.
     */
    public boolean checkHover(int mouseX, int mouseY, boolean canHover) {
        if (this.widget != null) {
            this.top = widget.y;
            this.bottom = widget.y + widget.getHeight();
            this.left = widget.x;
            this.right = widget.x + widget.getWidth();
            canHover = canHover && widget.visible;
        }

        if (canHover && hoverStart == -1 && mouseY >= top && mouseY <= bottom && mouseX >= left && mouseX <= right)
            hoverStart = System.currentTimeMillis();
        else if (!canHover || mouseY < top || mouseY > bottom || mouseX < left || mouseX > right)
            resetHoverTimer();

        return canHover && hoverStart != -1 && System.currentTimeMillis() - hoverStart >= threshold;
    }

    /**
     * Manually resets the hover timer.
     */
    public void resetHoverTimer() {
        hoverStart = -1;
    }
}
