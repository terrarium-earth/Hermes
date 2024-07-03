package earth.terrarium.hermes.elements.html;

import dev.dediamondpro.minemark.LayoutData;
import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.elements.ChildMovingElement;
import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.elements.Inline;
import dev.dediamondpro.minemark.elements.creators.ElementCreator;
import earth.terrarium.hermes.renderer.HermesRenderer;
import earth.terrarium.hermes.styles.HermesStyle;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

import java.awt.*;

public class HtmlDefault extends ChildMovingElement<HermesStyle, HermesRenderer> implements Inline {

    public static final ElementCreator<HermesStyle, HermesRenderer> CREATOR = new Creator();

    public HtmlDefault(@NotNull HermesStyle style, @NotNull LayoutStyle layoutStyle, @Nullable Element<HermesStyle, HermesRenderer> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);
    }

    @Override
    @ApiStatus.Internal
    public void generateLayout(LayoutData layoutData, HermesRenderer renderer) {
        if (Html.BLOCK.contains(qName)) {
            super.generateLayout(layoutData, renderer);
        } else {
            generateNewLayout(layoutData, renderer);
        }
    }

    @Override
    public void drawInternal(float xOffset, float yOffset, float mouseX, float mouseY, HermesRenderer renderData) {
        if (this.marker != null && this.marker.isInside(mouseX - xOffset, mouseY - yOffset)) {
            if (this.layoutStyle.get(AttributesGlobal.TITLE) != null) {
                renderData.setTooltip(Component.literal(this.layoutStyle.get(AttributesGlobal.TITLE)));
            }
            if (this.layoutStyle.get(AttributesGlobal.CURSOR) != null) {
                renderData.setCursor(this.layoutStyle.get(AttributesGlobal.CURSOR));
            }
        }
        super.drawInternal(xOffset, yOffset, mouseX, mouseY, renderData);
    }

    @Override
    protected void drawMarker(float x, float y, float markerWidth, float totalHeight, HermesRenderer renderer) {
        Color backgroundColor = this.layoutStyle.get(AttributesGlobal.BACKGROUND_COLOR);
        Color borderColor = this.layoutStyle.get(AttributesGlobal.BORDER_COLOR);

        if (backgroundColor != null) {
            renderer.fill(x, y, markerWidth, totalHeight, backgroundColor.getRGB());
        }
        if (borderColor != null) {
            renderer.fill(x, y, markerWidth, 2, borderColor.getRGB());
            renderer.fill(x, y + totalHeight - 2, markerWidth, 2, borderColor.getRGB());
            renderer.fill(x, y + 2, 2, totalHeight - 4, borderColor.getRGB());
            renderer.fill(x + markerWidth - 2, y + 2, 2, totalHeight - 4, borderColor.getRGB());
        }
    }

    @Override
    protected float getOutsidePadding(LayoutData layoutData, HermesRenderer renderData) {
        return super.getOutsidePadding(layoutData, renderData);
    }

    @Override
    protected float getInsidePadding(LayoutData layoutData, HermesRenderer renderData) {
        Color borderColor = this.layoutStyle.get(AttributesGlobal.BORDER_COLOR);
        return borderColor != null ? 4 : 0;
    }

    @Override
    protected float getMarkerWidth(LayoutData layoutData, HermesRenderer renderer) {
        return 0;
    }

    @Override
    protected MarkerType getMarkerType() {
        return MarkerType.BLOCK;
    }

    private static class Creator implements ElementCreator<HermesStyle, HermesRenderer> {

        @Override
        public Element<HermesStyle, HermesRenderer> createElement(HermesStyle style, LayoutStyle layoutStyle, @NotNull Element<HermesStyle, HermesRenderer> parent, @NotNull String qName, @NotNull Attributes attributes) {
            return new HtmlDefault(style, layoutStyle, parent, qName, attributes);
        }

        @Override
        public boolean appliesTo(HermesStyle style, LayoutStyle layoutStyle, @NotNull Element<HermesStyle, HermesRenderer> parent, @NotNull String qName, @NotNull Attributes attributes) {
            return true;
        }
    }
}