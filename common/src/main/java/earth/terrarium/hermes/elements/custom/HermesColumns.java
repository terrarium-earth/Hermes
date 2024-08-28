package earth.terrarium.hermes.elements.custom;

import dev.dediamondpro.minemark.LayoutData;
import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.utils.MouseButton;
import earth.terrarium.hermes.api.rendering.HtmlRenderer;
import earth.terrarium.hermes.api.rendering.HtmlStyle;
import earth.terrarium.hermes.elements.html.HtmlDefault;
import earth.terrarium.hermes.utils.AttributeParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

import java.util.Arrays;

public class HermesColumns extends HtmlDefault {

    private float[] template;
    private float[] widths;

    public HermesColumns(@NotNull HtmlStyle style, @NotNull LayoutStyle layoutStyle, @Nullable Element<HtmlStyle, HtmlRenderer> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);
        assert attributes != null;
        this.template = AttributeParser.parseFloats(attributes, "template", null);
    }

    @Override
    public void complete() {
        if (this.template == null) {
            this.template = new float[this.children.size()];
            Arrays.fill(this.template, 1f / this.template.length);
        }
        this.widths = new float[this.children.size()];
    }

    @Override
    public void generateLayout(LayoutData layoutData, HtmlRenderer renderer) {
        if (layoutData.isLineOccupied()) {
            layoutData.nextLine();
        }

        float height = 0f;

        for (int i = 0; i < this.children.size(); i++) {
            float width = this.template[i] * layoutData.getMaxWidth();
            LayoutData newLayoutData = new LayoutData(width);
            this.children.get(i).generateLayoutInternal(newLayoutData, renderer);
            height = Math.max(height, newLayoutData.getY() + newLayoutData.getCurrentLine().getHeight());
            this.widths[i] = newLayoutData.getMaxWidth();
        }

        layoutData.addElement(LayoutStyle.Alignment.LEFT, layoutData.getMaxWidth(), height);
        layoutData.nextLine();
    }

    @Override
    public void drawInternal(float xOffset, float yOffset, float mouseX, float mouseY, HtmlRenderer renderer) {
        drawDefault(xOffset, yOffset, mouseX, mouseY, renderer);
        float x = xOffset;
        for (int i = 0; i < this.children.size(); i++) {
            this.children.get(i).drawInternal(x, yOffset, mouseX, mouseY, renderer);
            x += this.widths[i];
        }
    }

    @Override
    public void beforeDrawInternal(float xOffset, float yOffset, float mouseX, float mouseY, HtmlRenderer renderData) {
        float x = xOffset;
        for (int i = 0; i < this.children.size(); i++) {
            this.children.get(i).beforeDrawInternal(x, yOffset, mouseX, mouseY, renderData);
            x += this.widths[i];
        }
    }

    @Override
    public void onMouseClickedInternal(MouseButton button, float mouseX, float mouseY) {
        float x = mouseX;
        for (int i = 0; i < this.children.size(); i++) {
            if (x < this.widths[i]) {
                this.children.get(i).onMouseClickedInternal(button, x, mouseY);
                return;
            }
            x -= this.widths[i];
        }
    }
}
