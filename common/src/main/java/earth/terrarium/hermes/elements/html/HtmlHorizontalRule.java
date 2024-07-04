package earth.terrarium.hermes.elements.html;

import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.elements.impl.HorizontalRuleElement;
import earth.terrarium.hermes.api.rendering.HtmlRenderer;
import earth.terrarium.hermes.api.rendering.HtmlStyle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

import java.awt.*;
import java.util.Objects;

public class HtmlHorizontalRule extends HorizontalRuleElement<HtmlStyle, HtmlRenderer> {

    public HtmlHorizontalRule(@NotNull HtmlStyle style, @NotNull LayoutStyle layoutStyle, @Nullable Element<HtmlStyle, HtmlRenderer> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);
    }

    @Override
    protected void drawLine(float x, float y, float width, float height, Color color, HtmlRenderer renderer) {
        color = Objects.requireNonNullElse(layoutStyle.get(GlobalAttributesElement.BACKGROUND_COLOR), color);
        renderer.fill(x, y, width, height, color.getRGB());
    }
}
