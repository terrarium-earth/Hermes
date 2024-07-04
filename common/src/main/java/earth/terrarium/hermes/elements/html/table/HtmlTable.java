package earth.terrarium.hermes.elements.html.table;

import dev.dediamondpro.minemark.LayoutData;
import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.elements.ChildBasedElement;
import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.elements.impl.table.TableRowElement;
import earth.terrarium.hermes.api.rendering.HtmlRenderer;
import earth.terrarium.hermes.api.rendering.HtmlStyle;
import earth.terrarium.hermes.utils.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

import java.util.List;

public class HtmlTable extends ChildBasedElement<HtmlStyle, HtmlRenderer> {

    private List<Element<HtmlStyle, HtmlRenderer>> rows;

    public HtmlTable(@NotNull HtmlStyle style, @NotNull LayoutStyle layoutStyle, @Nullable Element<HtmlStyle, HtmlRenderer> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);
    }

    @Override
    public void complete() {
        this.rows = Utils.filter(this.getChildren(), element -> element instanceof TableRowElement<HtmlStyle, HtmlRenderer>);
        this.children.sort(HtmlCaption.COMPARATOR);
    }

    public int getRowIndex(Element<HtmlStyle, HtmlRenderer> row) {
        return this.rows.indexOf(row);
    }

    @Override
    protected float getPadding(LayoutData layoutData, HtmlRenderer renderer) {
        return this.style.getTableStyle().getOutsidePadding();
    }
}
