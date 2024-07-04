package earth.terrarium.hermes.elements.html;

import dev.dediamondpro.minemark.LayoutData;
import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.elements.BasicElement;
import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.elements.Inline;
import dev.dediamondpro.minemark.utils.ColorFactory;
import earth.terrarium.hermes.api.rendering.HtmlRenderer;
import earth.terrarium.hermes.api.rendering.HtmlStyle;
import earth.terrarium.hermes.utils.AttributeParser;
import earth.terrarium.hermes.utils.CssParser;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

import java.awt.*;

public class HtmlProgress extends BasicElement<HtmlStyle, HtmlRenderer> implements Inline {

    private final float max;
    private final float value;

    private final Color borderColor;
    private final Color backgroundColor;
    private final Color barColor;

    public HtmlProgress(@NotNull HtmlStyle style, @NotNull LayoutStyle layoutStyle, @Nullable Element<HtmlStyle, HtmlRenderer> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);
        assert attributes != null;

        this.max = AttributeParser.parseFloat(attributes, "max", 1f);
        this.value = AttributeParser.parseFloat(attributes, "value", 0f);

        var css = CssParser.parseInlineCss(attributes.getValue("style"));
        this.borderColor = ColorFactory.web(css.getOrDefault("border-color", "gray"));
        this.backgroundColor = ColorFactory.web(css.getOrDefault("background-color", "white"));
        this.barColor = ColorFactory.web(css.getOrDefault("color", "dodgerblue"));
    }

    @Override
    protected void drawElement(float x, float y, float width, float height, HtmlRenderer renderer) {
        renderer.fill(x + 1, y, width - 2, 1, this.borderColor.getRGB()); // top
        renderer.fill(x + 1, y + height - 1, width - 2, 1, this.borderColor.getRGB()); // bottom
        renderer.fill(x, y + 1, 1, height - 2, this.borderColor.getRGB()); // left
        renderer.fill(x + width - 1, y + 1, 1, height - 2, this.borderColor.getRGB()); // right

        x += 1;
        y += 1;
        width -= 2;
        height -= 2;

        float barWidth = width * Mth.clamp(this.value / this.max, 0f, 1f);

        renderer.fill(x, y, width, height, this.backgroundColor.getRGB());
        renderer.fill(x, y, barWidth, height, this.barColor.getRGB());
    }

    @Override
    protected float getWidth(LayoutData layoutData, HtmlRenderer renderer) {
        return renderer.width(" ", this.layoutStyle.getFontSize()) * 20f;
    }

    @Override
    protected float getHeight(LayoutData layoutData, HtmlRenderer renderer) {
        return 8f * this.layoutStyle.getFontSize();
    }
}
