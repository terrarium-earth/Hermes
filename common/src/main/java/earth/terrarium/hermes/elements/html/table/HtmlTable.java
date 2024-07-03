package earth.terrarium.hermes.elements.html.table;

import dev.dediamondpro.minemark.LayoutData;
import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.elements.ChildBasedElement;
import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.elements.impl.table.TableRowElement;
import earth.terrarium.hermes.renderer.HermesRenderer;
import earth.terrarium.hermes.styles.HermesStyle;
import earth.terrarium.hermes.utils.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

import java.util.List;

public class HtmlTable extends ChildBasedElement<HermesStyle, HermesRenderer> {

    private List<Element<HermesStyle, HermesRenderer>> rows;

    public HtmlTable(@NotNull HermesStyle style, @NotNull LayoutStyle layoutStyle, @Nullable Element<HermesStyle, HermesRenderer> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);
    }

    @Override
    public void complete() {
        this.rows = Utils.filter(this.getChildren(), element -> element instanceof TableRowElement<HermesStyle, HermesRenderer>);
        this.children.sort(HtmlCaption.COMPARATOR);
    }

    public int getRowIndex(Element<HermesStyle, HermesRenderer> row) {
        return this.rows.indexOf(row);
    }

    protected float getPadding(LayoutData layoutData, HermesRenderer renderer) {
        return this.style.getTableStyle().getOutsidePadding();
    }
}
