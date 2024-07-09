package earth.terrarium.hermes.elements.html;

import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.elements.impl.CodeBlockElement;
import earth.terrarium.hermes.api.rendering.HtmlRenderer;
import earth.terrarium.hermes.api.rendering.HtmlStyle;
import earth.terrarium.hermes.impl.HermesRenderer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

import java.awt.*;

public class HtmlCodeBlock extends CodeBlockElement<HtmlStyle, HtmlRenderer> {

    public HtmlCodeBlock(@NotNull HtmlStyle style, @NotNull LayoutStyle layoutStyle, @Nullable Element<HtmlStyle, HtmlRenderer> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);
        this.isInline = isInline();
    }

    @Override
    protected void drawBlock(float x, float y, float width, float height, Color color, HtmlRenderer renderer) {
        HermesRenderer.drawDefault(x, y, width, height, color, this.layoutStyle, renderer);
    }
}
