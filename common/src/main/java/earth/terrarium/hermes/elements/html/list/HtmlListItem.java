package earth.terrarium.hermes.elements.html.list;

import dev.dediamondpro.minemark.LayoutData;
import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.elements.ChildMovingElement;
import dev.dediamondpro.minemark.elements.Element;
import earth.terrarium.hermes.api.rendering.HtmlRenderer;
import earth.terrarium.hermes.api.rendering.HtmlStyle;
import earth.terrarium.hermes.utils.Numerals;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

public class HtmlListItem extends ChildMovingElement<HtmlStyle, HtmlRenderer> {

    protected final int elementIndex;
    private final String prefix;

    protected float actualMarkerWidth;

    public HtmlListItem(@NotNull HtmlStyle style, @NotNull LayoutStyle layoutStyle, @Nullable Element<HtmlStyle, HtmlRenderer> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);

        if (!(parent instanceof HtmlList list)) {
            this.prefix = "• ";
            this.elementIndex = 0;
            return;
        }
        this.elementIndex = list.getChildren().indexOf(this);
        prefix = switch (list.getStyle()) {
            case LOWER_ALPHA -> Numerals.toAlpha(elementIndex + list.getStart() + 1, Numerals.Casing.LOWER) + ". ";
            case UPPER_ALPHA -> Numerals.toAlpha(elementIndex + list.getStart() + 1, Numerals.Casing.UPPER) + ". ";
            case LOWER_ROMAN -> Numerals.toRoman(elementIndex + list.getStart() + 1, Numerals.Casing.LOWER) + ". ";
            case UPPER_ROMAN -> Numerals.toRoman(elementIndex + list.getStart() + 1, Numerals.Casing.UPPER) + ". ";
            case NUMBER -> elementIndex + list.getStart() + 1 + ". ";
            case CIRCLE -> "○ ";
            case SQUARE -> "■ ";
            case DISC -> "⏺ ";
            case null, default -> "";
        };
    }

    protected ChildMovingElement.MarkerType getMarkerType() {
        return MarkerType.ONE_LINE;
    }

    @Override
    protected void drawMarker(float x, float y, float markerWidth, float totalHeight, HtmlRenderer renderer) {
        renderer.drawString(
                prefix,
                x + markerWidth - this.actualMarkerWidth + renderer.width(prefix, layoutStyle.getFontSize()) / 2f,
                y,
                layoutStyle.getFontSize(),
                layoutStyle.getTextColor().getRGB(),
                false
        );
    }

    @Override
    protected float getOutsidePadding(LayoutData layoutData, HtmlRenderer renderData) {
        return this.style.getTextStyle().getPadding();
    }

    @Override
    protected float getInsidePadding(LayoutData layoutData, HtmlRenderer renderData) {
        return 0f;
    }

    @Override
    protected float getMarkerWidth(LayoutData layout, HtmlRenderer renderer) {
        this.actualMarkerWidth = Math.max(renderer.width(prefix, layoutStyle.getFontSize()), style.getListStyle().getIndentation());
        if (qName.equals("dt")) {
            this.actualMarkerWidth = 0;
        }
        return this.actualMarkerWidth;
    }

    @Override
    protected float getMarkerHeight(LayoutData layoutData, HtmlRenderer renderData) {
        return 8f * layoutStyle.getFontSize();
    }
}
