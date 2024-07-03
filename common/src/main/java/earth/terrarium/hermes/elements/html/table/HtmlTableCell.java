package earth.terrarium.hermes.elements.html.table;

import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.elements.impl.table.TableCellElement;
import earth.terrarium.hermes.renderer.HermesRenderer;
import earth.terrarium.hermes.styles.HermesStyle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

import java.awt.*;

public class HtmlTableCell extends TableCellElement<HermesStyle, HermesRenderer> {

    public HtmlTableCell(@NotNull HermesStyle style, @NotNull LayoutStyle layoutStyle, @Nullable Element<HermesStyle, HermesRenderer> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);
    }

    @Override
    protected void drawCellBackground(float x, float y, float width, float height, Color color, HermesRenderer renderer) {
        renderer.fill(x, y, width, height, color.getRGB());
    }

    @Override
    protected void drawBorderLine(float x, float y, float width, float height, Color color, HermesRenderer renderer) {
        renderer.fill(x, y, width, height, color.getRGB());
    }

    @Override
    public void beforeDrawInternal(float xOffset, float yOffset, float mouseX, float mouseY, HermesRenderer renderer) {
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
