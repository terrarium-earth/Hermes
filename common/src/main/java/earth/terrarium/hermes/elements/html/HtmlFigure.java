package earth.terrarium.hermes.elements.html;

import dev.dediamondpro.minemark.LayoutData;
import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.elements.ChildMovingElement;
import dev.dediamondpro.minemark.elements.Element;
import earth.terrarium.hermes.renderer.HermesRenderer;
import earth.terrarium.hermes.styles.HermesStyle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

public class HtmlFigure extends ChildMovingElement<HermesStyle, HermesRenderer> {

    public HtmlFigure(@NotNull HermesStyle style, @NotNull LayoutStyle layoutStyle, @Nullable Element<HermesStyle, HermesRenderer> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);
    }

    @Override
    protected void drawMarker(float x, float y, float markerWidth, float totalHeight, HermesRenderer renderData) {

    }

    @Override
    protected float getMarkerWidth(LayoutData layoutData, HermesRenderer renderData) {
        return 0;
    }

    @Override
    protected float getOutsidePadding(LayoutData layoutData, HermesRenderer renderData) {
        return 9f * this.layoutStyle.getFontSize();
    }
}
