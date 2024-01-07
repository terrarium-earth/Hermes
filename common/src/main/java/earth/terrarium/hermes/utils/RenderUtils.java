package earth.terrarium.hermes.utils;

import net.minecraft.client.gui.GuiGraphics;

public final class RenderUtils {

    public static void renderOutline(GuiGraphics graphics, int x, int y, int outerWidth, int outerHeight, int color, int thickness) {

        // Like graphics.renderOutline(), but also takes a thickness argument.
        // Outline segments are drawn to avoid over-lap in case color's alpha < 0xFF

        int xA = x; int xB = x + outerWidth;
        int yA = y; int yB = y + outerHeight;
        graphics.fill(xA, yA, xB, yA + thickness, color); // top
        graphics.fill(xA, yB, xB, yB - thickness, color); // bottom

        yA += thickness; yB -= thickness;
        graphics.fill(xA, yA, xA + thickness, yB, color); // left
        graphics.fill(xB, yA, xB - thickness, yB, color); // right
    }
}
