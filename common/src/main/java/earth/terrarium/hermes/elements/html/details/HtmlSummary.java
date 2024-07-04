package earth.terrarium.hermes.elements.html.details;

import dev.dediamondpro.minemark.LayoutData;
import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.elements.BasicElement;
import dev.dediamondpro.minemark.elements.ChildBasedElement;
import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.elements.Inline;
import dev.dediamondpro.minemark.utils.MouseButton;
import earth.terrarium.hermes.api.rendering.HtmlRenderer;
import earth.terrarium.hermes.api.rendering.HtmlStyle;
import earth.terrarium.hermes.utils.MarkdownPositions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;

public class HtmlSummary extends ChildBasedElement<HtmlStyle, HtmlRenderer> {

    private final MarkdownPositions positions = new MarkdownPositions();
    private boolean isHeading;

    public HtmlSummary(@NotNull HtmlStyle style, @NotNull LayoutStyle layoutStyle, @Nullable Element<HtmlStyle, HtmlRenderer> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);
    }

    public void setAsHeading() {
        this.isHeading = true;
        var chevron = new Chevron(style, layoutStyle, this, "chevron", new AttributesImpl());
        this.children.remove(chevron);
        this.children.addFirst(chevron);
    }

    @Override
    public void generateLayout(LayoutData layoutData, HtmlRenderer renderer) {
        this.positions.init(layoutData, renderer, super::generateLayout);
    }

    @Override
    public void onMouseClickedInternal(MouseButton button, float mouseX, float mouseY) {
        if (!(parent instanceof HtmlDetails details)) return;
        if (!this.isHeading) return;
        if (button != MouseButton.LEFT) return;
        if (!positions.isAnyInside(mouseX, mouseY)) return;

        details.toggle();
    }

    private static class Chevron extends BasicElement<HtmlStyle, HtmlRenderer> implements Inline {

        public Chevron(@NotNull HtmlStyle style, @NotNull LayoutStyle layoutStyle, @Nullable Element<HtmlStyle, HtmlRenderer> parent, @NotNull String qName, @Nullable Attributes attributes) {
            super(style, layoutStyle, parent, qName, attributes);
        }

        @Override
        protected void drawElement(float x, float y, float width, float height, HtmlRenderer renderData) {
            if (!(parent instanceof HtmlSummary)) return;
            if (!(parent.getParent() instanceof HtmlDetails details)) return;

            renderData.drawString(
                    details.open() ? "▼ " : "▶ ",
                    x, y,
                    layoutStyle.getFontSize(), layoutStyle.getTextColor().getRGB(),
                    false
            );
        }

        @Override
        protected float getWidth(LayoutData layoutData, HtmlRenderer renderer) {
            return renderer.width("▶ ", this.layoutStyle.getFontSize());
        }

        @Override
        protected float getHeight(LayoutData layoutData, HtmlRenderer renderer) {
            return 8f * this.layoutStyle.getFontSize();
        }
    }
}
