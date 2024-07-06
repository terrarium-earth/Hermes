package earth.terrarium.hermes.elements.html;

import dev.dediamondpro.minemark.LayoutData;
import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.elements.ChildMovingElement;
import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.elements.Inline;
import dev.dediamondpro.minemark.elements.creators.ElementCreator;
import earth.terrarium.hermes.api.rendering.HtmlRenderer;
import earth.terrarium.hermes.api.rendering.HtmlStyle;
import earth.terrarium.hermes.impl.HermesRenderer;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

public class HtmlDefault extends ChildMovingElement<HtmlStyle, HtmlRenderer> implements Inline {

    public static final ElementCreator<HtmlStyle, HtmlRenderer> CREATOR = new Creator();

    public HtmlDefault(@NotNull HtmlStyle style, @NotNull LayoutStyle layoutStyle, @Nullable Element<HtmlStyle, HtmlRenderer> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);
    }

    @Override
    @ApiStatus.Internal
    public void generateLayout(LayoutData layoutData, HtmlRenderer renderer) {
        if (Html.BLOCK.contains(qName)) {
            super.generateLayout(layoutData, renderer);
        } else {
            generateNewLayout(layoutData, renderer);
        }
    }

    @Override
    public void drawInternal(float xOffset, float yOffset, float mouseX, float mouseY, HtmlRenderer renderer) {
        if (this.marker != null && this.marker.isInside(mouseX - xOffset, mouseY - yOffset)) {
            if (this.layoutStyle.get(GlobalAttributesElement.TITLE) != null) {
                renderer.setTooltip(Component.literal(this.layoutStyle.get(GlobalAttributesElement.TITLE)));
            }
            if (this.layoutStyle.get(GlobalAttributesElement.CURSOR) != null) {
                renderer.setCursor(this.layoutStyle.get(GlobalAttributesElement.CURSOR));
            }
        }
        super.drawInternal(xOffset, yOffset, mouseX, mouseY, renderer);
    }

    @Override
    protected void drawMarker(float x, float y, float markerWidth, float totalHeight, HtmlRenderer renderer) {
        HermesRenderer.drawDefault(x, y, markerWidth, totalHeight, this.layoutStyle, renderer);
    }

    @Override
    protected float getOutsidePadding(LayoutData layoutData, HtmlRenderer renderer) {
        return super.getOutsidePadding(layoutData, renderer);
    }

    @Override
    protected float getInsidePadding(LayoutData layoutData, HtmlRenderer renderer) {
        return this.layoutStyle.getOrDefault(GlobalAttributesElement.PADDING, 0f);
    }

    @Override
    protected float getMarkerWidth(LayoutData layoutData, HtmlRenderer renderer) {
        return 0;
    }

    @Override
    protected MarkerType getMarkerType() {
        return MarkerType.BLOCK;
    }

    private static class Creator implements ElementCreator<HtmlStyle, HtmlRenderer> {

        @Override
        public Element<HtmlStyle, HtmlRenderer> createElement(HtmlStyle style, LayoutStyle layoutStyle, @NotNull Element<HtmlStyle, HtmlRenderer> parent, @NotNull String qName, @NotNull Attributes attributes) {
            return new HtmlDefault(style, layoutStyle, parent, qName, attributes);
        }

        @Override
        public boolean appliesTo(HtmlStyle style, LayoutStyle layoutStyle, @NotNull Element<HtmlStyle, HtmlRenderer> parent, @NotNull String qName, @NotNull Attributes attributes) {
            return true;
        }
    }
}