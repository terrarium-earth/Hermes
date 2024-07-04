package earth.terrarium.hermes.elements.html;

import dev.dediamondpro.minemark.LayoutData;
import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.elements.ChildMovingElement;
import dev.dediamondpro.minemark.elements.Element;
import earth.terrarium.hermes.api.rendering.HtmlRenderer;
import earth.terrarium.hermes.api.rendering.HtmlStyle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

public class HtmlFigure extends ChildMovingElement<HtmlStyle, HtmlRenderer> {

    public HtmlFigure(@NotNull HtmlStyle style, @NotNull LayoutStyle layoutStyle, @Nullable Element<HtmlStyle, HtmlRenderer> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);
    }

    @Override
    protected void drawMarker(float x, float y, float markerWidth, float totalHeight, HtmlRenderer renderer) {

    }

    @Override
    protected float getMarkerWidth(LayoutData layoutData, HtmlRenderer renderer) {
        return 0;
    }

    @Override
    protected float getOutsidePadding(LayoutData layoutData, HtmlRenderer renderer) {
        return 9f * this.layoutStyle.getFontSize();
    }
}
