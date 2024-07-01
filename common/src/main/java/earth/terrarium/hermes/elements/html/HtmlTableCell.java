package earth.terrarium.hermes.elements.html;

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
}
