package earth.terrarium.hermes.api.defaults;

import com.teamresourceful.resourcefullib.common.color.Color;
import com.teamresourceful.resourcefullib.common.color.ConstantColors;
import earth.terrarium.hermes.api.TagElement;
import earth.terrarium.hermes.utils.ElementParsingUtils;
import net.minecraft.client.gui.GuiGraphics;

import java.util.Map;
import java.util.function.IntFunction;

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
    protected int xMargin; // (background + borderWidth);
    protected int yMargin; // 1 or (backgroundPadding + borderWidth)

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
        this.xMargin = backgroundPadding + borderWidth;
        this.yMargin = Math.max(1, (backgroundPadding + borderWidth));
    }

    public void drawFillAndBorder(GuiGraphics graphics, int x, int y, float width, float height) {

        IntFunction<Integer> no00Alpha = (c) -> (c >> 24) != 0 ? c : c + (0xFF << 24);

        int xA = x - backgroundPadding;
        int yA = y - backgroundPadding;
        int xB = Math.round(x + width + backgroundPadding);
        int yB = Math.round(y + height + backgroundPadding);

        if (hasBackground) {
            graphics.fill(xA, yA, xB, yB, no00Alpha.apply(backgroundColor.getValue()));
        }
        if (hasBorder) {
            int color = no00Alpha.apply(borderColor.getValue());

            xA -= borderWidth; yA -= borderWidth;
            xB += borderWidth; yB += borderWidth;
            graphics.fill(xA, yA, xB, yA + borderWidth, color); // top
            graphics.fill(xA, yB, xB, yB - borderWidth, color); // bottom

            yA += borderWidth; yB -= borderWidth; // avoid over-lap in case color's alpha < 0xFF
            graphics.fill(xA, yA, xA + borderWidth, yB, color); // left
            graphics.fill(xB, yA, xB - borderWidth, yB, color); // right
        }
    }

}
