package earth.terrarium.hermes.elements.html;

import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.elements.impl.HorizontalRuleElement;
import earth.terrarium.hermes.renderer.HermesRenderer;
import earth.terrarium.hermes.styles.HermesStyle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

import java.awt.*;
import java.util.Objects;

public class HtmlHorizontalRule extends HorizontalRuleElement<HermesStyle, HermesRenderer> {

    public HtmlHorizontalRule(@NotNull HermesStyle style, @NotNull LayoutStyle layoutStyle, @Nullable Element<HermesStyle, HermesRenderer> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);
    }

    @Override
    protected void drawLine(float x, float y, float width, float height, Color color, HermesRenderer renderer) {
        color = Objects.requireNonNullElse(layoutStyle.get(AttributesGlobal.BACKGROUND_COLOR), color);
        renderer.fill(x, y, width, height, color.getRGB());
    }
}
