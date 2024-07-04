package earth.terrarium.hermes.elements.html.table;

import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.elements.impl.table.TableCellElement;
import earth.terrarium.hermes.api.rendering.HtmlRenderer;
import earth.terrarium.hermes.api.rendering.HtmlStyle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

import java.awt.*;

public class HtmlTableCell extends TableCellElement<HtmlStyle, HtmlRenderer> {

    public HtmlTableCell(@NotNull HtmlStyle style, @NotNull LayoutStyle layoutStyle, @Nullable Element<HtmlStyle, HtmlRenderer> parent, @NotNull String qName, @NotNull Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);
    }

    @Override
    protected void drawCellBackground(float x, float y, float width, float height, Color color, HtmlRenderer renderer) {
        renderer.fill(x, y, width, height, color.getRGB());
    }

    @Override
    protected void drawBorderLine(float x, float y, float width, float height, Color color, HtmlRenderer renderer) {
        renderer.fill(x, y, width, height, color.getRGB());
    }

    @Override
    public void beforeDrawInternal(float xOffset, float yOffset, float mouseX, float mouseY, HtmlRenderer renderer) {
        boolean setIndex = this.cellHeight == -1.0F;
        super.beforeDrawInternal(xOffset, yOffset, mouseX, mouseY, renderer);
        if (setIndex) {
            assert this.parent != null;
            assert this.parent.getParent() != null;
            if (!(this.parent.getParent() instanceof HtmlTable table)) return;

            this.rowIndex = table.getRowIndex(this.parent);
            this.cellIndex = this.parent.getChildren().indexOf(this);
            this.fillColor = this.rowIndex % 2 == 0 ? this.style.getTableStyle().getEvenFillColor() : this.style.getTableStyle().getOddFillColor();
        }
    }
}
