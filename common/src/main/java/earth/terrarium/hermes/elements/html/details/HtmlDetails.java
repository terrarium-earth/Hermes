package earth.terrarium.hermes.elements.html.details;

import dev.dediamondpro.minemark.LayoutData;
import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.elements.ChildBasedElement;
import dev.dediamondpro.minemark.elements.Element;
import earth.terrarium.hermes.api.rendering.HtmlRenderer;
import earth.terrarium.hermes.api.rendering.HtmlStyle;
import earth.terrarium.hermes.elements.html.HtmlParagraph;
import earth.terrarium.hermes.utils.AttributeParser;
import earth.terrarium.hermes.utils.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;

import java.util.List;
import java.util.Objects;

public class HtmlDetails extends ChildBasedElement<HtmlStyle, HtmlRenderer> {

    private boolean open;
    private List<Element<HtmlStyle, HtmlRenderer>> copy = null;

    public HtmlDetails(@NotNull HtmlStyle style, @NotNull LayoutStyle layoutStyle, @Nullable Element<HtmlStyle, HtmlRenderer> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);
        assert attributes != null;

        this.open = AttributeParser.parseBoolean(attributes, "open", false);
    }

    @Override
    public void complete() {
        Element<HtmlStyle, HtmlRenderer> summary = Utils.findFirst(this.children, element -> element instanceof HtmlSummary);
        if (summary == null) {
            summary = new HtmlSummary(
                    style, layoutStyle,
                    this,
                    "summary",
                    new AttributesImpl()
            );
            new HtmlParagraph(
                    Objects.requireNonNullElse(attributes.getValue("summary"), "Details"),
                    style, layoutStyle,
                    summary,
                    "text", null
            );
        }

        this.children.remove(summary);
        this.children.addFirst(summary);

        this.copy = List.copyOf(this.children);

        if (summary instanceof HtmlSummary s) {
            s.setAsHeading();
        }
    }

    public boolean open() {
        return this.open;
    }

    @Override
    public void generateLayout(LayoutData layoutData, HtmlRenderer renderer) {
        if (this.open) {
            this.children.clear();
            this.children.addAll(this.copy);
        } else {
            this.children.clear();
            this.children.add(this.copy.getFirst());
        }

        super.generateLayout(layoutData, renderer);
    }

    public void toggle() {
        this.open = !this.open;
        this.regenerateLayout();
    }
}
