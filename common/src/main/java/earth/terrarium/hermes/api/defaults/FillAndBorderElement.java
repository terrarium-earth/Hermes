package earth.terrarium.hermes.api.defaults;

import com.teamresourceful.resourcefullib.common.color.Color;
import com.teamresourceful.resourcefullib.common.color.ConstantColors;
import earth.terrarium.hermes.api.TagElement;
import earth.terrarium.hermes.utils.ElementParsingUtils;
import earth.terrarium.hermes.utils.RenderUtils;
import net.minecraft.client.gui.GuiGraphics;

import java.util.Map;

public abstract class FillAndBorderElement implements TagElement {

    /* For background fills and borders.
    Though terms are borrowed from CSS's Box Model, does not change Hermes' layout model.
    Does allow inheriting elements to be set taller and/or wider.
    The area behind the element, plus any padding, may be filled with background color
    A border may surround the padding area, with its own width and color. */

    protected int backgroundPadding = 0;
    protected Color backgroundColor = ConstantColors.gray;
    protected boolean hasBackground = false;
    protected int borderWidth = 0;
    protected Color borderColor = ConstantColors.whitesmoke;
    protected boolean hasBorder = false;
    protected int xSurround; // (background + borderWidth);
    protected int ySurround; // 1 or (backgroundPadding + borderWidth)

    protected FillAndBorderElement(Map<String, String> parameters) {
        if (parameters.containsKey("background")) {
            this.hasBackground = true;
            String[] backgroundSpecs = parameters.get("background").split(" ");
            switch (backgroundSpecs.length) {
                case 2:
                    this.backgroundColor = ElementParsingUtils.tryParse(backgroundSpecs[1], Color::parse, backgroundColor);
                case 1:
                    this.backgroundPadding = ElementParsingUtils.tryParse(backgroundSpecs[0], Integer::parseInt, backgroundPadding);
            }
        }
        if (parameters.containsKey("border")) {
            this.hasBorder = true;
            this.borderWidth = 1; // Set a default value when the tag has a border attribute.
            String[] borderSpecs = parameters.get("border").split(" ");
            switch (borderSpecs.length) {
                case 2:
                    this.borderColor = ElementParsingUtils.tryParse(borderSpecs[1], Color::parse, borderColor);
                case 1:
                    this.borderWidth = ElementParsingUtils.tryParse(borderSpecs[0], Integer::parseInt, borderWidth);
            }
        }
        this.xSurround = backgroundPadding + borderWidth;
        this.ySurround = Math.max(1, (backgroundPadding + borderWidth));
    }

    static int highPassAlpha(int color) {
        return (color >> 24) != 0 ? color : color + (0xFF << 24);
    };

    public void drawFillAndBorder(GuiGraphics graphics, int x, int y, float width, float height) {

        int xA = x - backgroundPadding;
        int yA = y - backgroundPadding;
        int xB = Math.round(x + width + backgroundPadding);
        int yB = Math.round(y + height + backgroundPadding);

        if (hasBackground) {
            graphics.fill(xA, yA, xB, yB, highPassAlpha(backgroundColor.getValue()));
        }
        if (hasBorder) {
            int color = highPassAlpha(borderColor.getValue());
            xA -= borderWidth; yA -= borderWidth;
            xB += borderWidth; yB += borderWidth;
            RenderUtils.renderOutline(graphics, xA, yA, xB - xA, yB - yA, color, borderWidth);
        }
    }

}
