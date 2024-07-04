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

import java.util.Objects;

public class HtmlBlockQuote extends ChildMovingElement<HermesStyle, HermesRenderer> {

    public HtmlBlockQuote(@NotNull HermesStyle style, @NotNull LayoutStyle layoutStyle, @Nullable Element<HermesStyle, HermesRenderer> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);
    }

    @Override
    protected void drawMarker(float x, float y, float markerWidth, float totalHeight, HermesRenderer renderer) {
        x = x + style.getBlockquoteStyle().getSpacingLeft();
        renderer.fill(
                x + style.getBlockquoteStyle().getBlockWidth(),
                y,
                markerWidth - style.getBlockquoteStyle().getSpacingRight(),
                totalHeight,
                Objects.requireNonNullElse(
                        layoutStyle.get(AttributesGlobal.BACKGROUND_COLOR),
                        style.getBlockquoteStyle().getBackgroundColor()
                ).getRGB()
        );
        renderer.fill(
                x, y,
                style.getBlockquoteStyle().getBlockWidth(),
                totalHeight,
                Objects.requireNonNullElse(
                        layoutStyle.get(AttributesGlobal.BORDER_COLOR),
                        style.getBlockquoteStyle().getBlockColor()
                ).getRGB()
        );
    }

    @Override
    protected float getMarkerWidth(LayoutData layoutData, HermesRenderer renderer) {
        return style.getBlockquoteStyle().getSpacingLeft()
                + style.getBlockquoteStyle().getBlockWidth()
                + style.getBlockquoteStyle().getSpacingRight();
    }

    @Override
    protected float getOutsidePadding(LayoutData layoutData, HermesRenderer renderData) {
        return style.getBlockquoteStyle().getPadding();
    }

    @Override
    protected float getInsidePadding(LayoutData layoutData, HermesRenderer renderData) {
        return style.getBlockquoteStyle().getPadding() + style.getBlockquoteStyle().getSpacingLeft();
    }

    @Override
    protected MarkerType getMarkerType() {
        return MarkerType.BLOCK;
    }
}
