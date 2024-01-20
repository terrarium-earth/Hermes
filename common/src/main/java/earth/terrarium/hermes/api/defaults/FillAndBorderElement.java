package earth.terrarium.hermes.api.defaults;

import com.teamresourceful.resourcefullib.common.color.Color;
import com.teamresourceful.resourcefullib.common.color.ConstantColors;
import earth.terrarium.hermes.api.TagElement;
import earth.terrarium.hermes.utils.ElementParsingUtils;
import earth.terrarium.hermes.utils.RenderUtils;
import it.unimi.dsi.fastutil.ints.IntObjectPair;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public abstract class FillAndBorderElement implements TagElement {

    /* For background fills and borders.
    Though terms are borrowed from CSS's Box Model, does not change Hermes' layout model.
    Does allow inheriting elements to be set taller and/or wider.
    The area behind the element, plus any padding, may be filled with background color
    A border may surround the padding area, with its own width and color. */

    protected @Nullable IntObjectPair<Color> fill;
    protected @Nullable IntObjectPair<Color> border;
    protected int xSurround; // (background + borderWidth);
    protected int ySurround; // 1 or (backgroundPadding + borderWidth)

    protected FillAndBorderElement(Map<String, String> parameters) {

        this.fill = ElementParsingUtils.parsePair(
                parameters, "background", IntObjectPair::of,
                Integer::parseInt, 0,
                Color::parse, ConstantColors.gainsboro
        );

        this.border = ElementParsingUtils.parsePair(
                parameters, "border", IntObjectPair::of,
                Integer::parseInt, 0,
                Color::parse, ConstantColors.whitesmoke
        );

        if (fill != null) xSurround += fill.firstInt();
        if (border != null) xSurround += border.firstInt();
        this.ySurround = Math.max(1, xSurround);
    }

    /** If the alpha channel is 0, make it 255. */
    private static int ensureVisible(Color color) {
        int value = color.getValue();
        return (value >> 24) != 0 ? value : value | 0xFF000000;
    }

    public void drawFillAndBorder(GuiGraphics graphics, int x, int y, float width, float height) {

        if (fill != null) {
            int padding = fill.firstInt();
            int x0 = x - padding;
            int y0 = y - padding;
            int x1 = Math.round(x + width + padding);
            int y1 = Math.round(y + height + padding);
            graphics.fill(x0, y0, x1, y1, ensureVisible(fill.second()));
        }

        if (border != null) {
            int x0 = x - xSurround;
            int y0 = y - ySurround;
            int x1 = Math.round(x + width + xSurround);
            int y1 = Math.round(y + height + ySurround);
            int color = ensureVisible(border.second());
            RenderUtils.renderOutline(graphics, x0, y0, x1 - x0, y1 - y0, border.firstInt(), color);
        }
    }
}
