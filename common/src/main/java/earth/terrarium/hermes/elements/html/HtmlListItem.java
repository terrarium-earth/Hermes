package earth.terrarium.hermes.elements.html;

import dev.dediamondpro.minemark.LayoutData;
import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.elements.impl.list.ListElement;
import dev.dediamondpro.minemark.elements.impl.list.ListHolderElement;
import earth.terrarium.hermes.renderer.HermesRenderer;
import earth.terrarium.hermes.styles.HermesStyle;
import earth.terrarium.hermes.utils.Numerals;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

public class HtmlListItem extends ListElement<HermesStyle, HermesRenderer> {

    private final String prefix;

    public HtmlListItem(@NotNull HermesStyle style, @NotNull LayoutStyle layoutStyle, @Nullable Element<HermesStyle, HermesRenderer> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);

        if (!(parent instanceof HtmlList list)) {
            prefix = listType == ListHolderElement.ListType.ORDERED ? elementIndex + 1 + "." : "• ";
            return;
        }
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

    @Override
    protected void drawMarker(float x, float y, HermesRenderer renderer) {
        renderer.drawString(
                prefix,
                x,
                y,
                layoutStyle.getFontSize(),
                layoutStyle.getTextColor().getRGB(),
                false
        );
    }

    @Override
    protected float getListMarkerWidth(LayoutData layout, HermesRenderer renderer) {
        float indent = Math.max(renderer.width(prefix, layoutStyle.getFontSize()), style.getListStyle().getIndentation());
        if (qName.equals("dd")) {
            return indent * 2f;
        }
        return indent;
    }

    @Override
    protected float getMarkerHeight(LayoutData layoutData, HermesRenderer renderData) {
        return 8f * layoutStyle.getFontSize();
    }
}
