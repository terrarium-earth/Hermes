package earth.terrarium.hermes.utils;

import net.minecraft.client.gui.GuiGraphics;

public final class RenderUtils {

    public static void renderOutline(GuiGraphics graphics, int x, int y, int outerWidth, int outerHeight, int thickness, int color) {

        // Like graphics.renderOutline(), but also takes a thickness argument.
        // Outline segments are drawn to avoid over-lap in case color's alpha < 0xFF

        int x0 = x; int x1 = x + outerWidth;
        int y0 = y; int y1 = y + outerHeight;
        graphics.fill(x0, y0, x1, y0 + thickness, color); // top
        graphics.fill(x0, y1, x1, y1 - thickness, color); // bottom

        y0 += thickness; y1 -= thickness;
        graphics.fill(x0, y0, x0 + thickness, y1, color); // left
        graphics.fill(x1, y0, x1 - thickness, y1, color); // right
    }
}
