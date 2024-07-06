package earth.terrarium.hermes.elements.html;

import dev.dediamondpro.minemark.LayoutData;
import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.elements.ChildMovingElement;
import dev.dediamondpro.minemark.elements.Element;
import earth.terrarium.hermes.api.rendering.HtmlRenderer;
import earth.terrarium.hermes.api.rendering.HtmlStyle;
import earth.terrarium.hermes.utils.CssBorder;
import net.minecraft.Optionull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

import java.util.Objects;

public class HtmlBlockQuote extends ChildMovingElement<HtmlStyle, HtmlRenderer> {

    public HtmlBlockQuote(@NotNull HtmlStyle style, @NotNull LayoutStyle layoutStyle, @Nullable Element<HtmlStyle, HtmlRenderer> parent, @NotNull String qName, @NotNull Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);
    }

    @Override
    protected void drawMarker(float x, float y, float markerWidth, float totalHeight, HtmlRenderer renderer) {
        x = x + style.getBlockquoteStyle().getSpacingLeft();
        renderer.fill(
                x + style.getBlockquoteStyle().getBlockWidth(),
                y,
                markerWidth - style.getBlockquoteStyle().getSpacingRight(),
                totalHeight,
                Objects.requireNonNullElse(
                        layoutStyle.get(GlobalAttributesElement.BACKGROUND_COLOR),
                        style.getBlockquoteStyle().getBackgroundColor()
                ).getRGB()
        );
        renderer.fill(
                x, y,
                style.getBlockquoteStyle().getBlockWidth(),
                totalHeight,
                Optionull.mapOrDefault(
                        layoutStyle.get(GlobalAttributesElement.BORDER),
                        CssBorder::getColor,
                        style.getBlockquoteStyle().getBlockColor().getRGB()
                )
        );
    }

    @Override
    protected float getMarkerWidth(LayoutData layoutData, HtmlRenderer renderer) {
        return style.getBlockquoteStyle().getSpacingLeft()
                + style.getBlockquoteStyle().getBlockWidth()
                + style.getBlockquoteStyle().getSpacingRight();
    }

    @Override
    protected float getOutsidePadding(LayoutData layoutData, HtmlRenderer renderData) {
        return style.getBlockquoteStyle().getPadding();
    }

    @Override
    protected float getInsidePadding(LayoutData layoutData, HtmlRenderer renderData) {
        return style.getBlockquoteStyle().getPadding() + style.getBlockquoteStyle().getSpacingLeft();
    }

    @Override
    protected MarkerType getMarkerType() {
        return MarkerType.BLOCK;
    }
}
