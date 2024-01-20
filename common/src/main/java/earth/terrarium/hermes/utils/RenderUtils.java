package earth.terrarium.hermes.utils;

import net.minecraft.client.gui.GuiGraphics;

public final class RenderUtils {

    public static void renderOutline(GuiGraphics graphics, int x0, int y0, int width, int height, int thickness, int color) {
        int x1 = x0 + width;
        int y1 = y0 + height;

        int xi0 = x0 + thickness;
        int yi0 = y0 + thickness;
        int xi1 = x1 - thickness;
        int yi1 = y1 - thickness;

        // Like graphics.renderOutline(), but also takes a thickness argument.
        // Outline segments are drawn to avoid over-lap in case color's alpha < 0xFF

        graphics.fill(x0, y0, x1, yi0, color); // top
        graphics.fill(x0, y1, x1, yi1, color); // bottom

        graphics.fill(x0, yi0, xi0, yi1, color); // left
        graphics.fill(x1, yi0, xi1, yi1, color); // right
    }
}
