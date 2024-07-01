package earth.terrarium.hermes.elements.html.details;

import dev.dediamondpro.minemark.LayoutData;
import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.elements.ChildBasedElement;
import dev.dediamondpro.minemark.elements.Element;
import earth.terrarium.hermes.elements.html.HtmlParagraph;
import earth.terrarium.hermes.renderer.HermesRenderer;
import earth.terrarium.hermes.styles.HermesStyle;
import earth.terrarium.hermes.utils.AttributeParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;

import java.util.List;
import java.util.Objects;

public class HtmlDetails extends ChildBasedElement<HermesStyle, HermesRenderer> {

    private boolean open;
    private List<Element<HermesStyle, HermesRenderer>> copy = null;

    public HtmlDetails(@NotNull HermesStyle style, @NotNull LayoutStyle layoutStyle, @Nullable Element<HermesStyle, HermesRenderer> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);
        assert attributes != null;

        this.open = AttributeParser.parseBoolean(attributes, "open", false);

        AttributesImpl defaultSummaryAttributes = new AttributesImpl();
        defaultSummaryAttributes.addAttribute(
                "", "default-details", "default-details", "CDATA", "true"
        );

        new HtmlParagraph(
                Objects.requireNonNullElse(attributes.getValue("summary"), "Details"),
                style, layoutStyle,
                new HtmlSummary.Default(
                        style, layoutStyle,
                        this,
                        "summary",
                        defaultSummaryAttributes
                ),
                "text", null
        );
    }

    public boolean open() {
        return this.open;
    }

    public HtmlSummary getSummary() {
        for (Element<HermesStyle, HermesRenderer> child : this.children) {
            if (child instanceof HtmlSummary summary) {
                return summary;
            }
        }
        return null;
    }

    @Override
    public void generateLayout(LayoutData layoutData, HermesRenderer renderData) {
        this.children.sort((a, b) -> {
            // sort by default then summary
            if (a instanceof HtmlSummary.Default) return -1;
            if (b instanceof HtmlSummary.Default) return 1;
            if (a instanceof HtmlSummary) return -1;
            if (b instanceof HtmlSummary) return 1;
            return 0;
        });

        if (
            this.children.size() > 2 &&
            this.children.get(1) instanceof HtmlSummary &&
            this.children.getFirst() instanceof HtmlSummary.Default
        ) {
            this.children.removeFirst().close();
        }

        if (this.copy == null) {
            this.copy = List.copyOf(this.children);
        }

        if (this.open) {
            this.children.clear();
            this.children.addAll(this.copy);
        } else {
            this.children.clear();
            this.children.add(this.copy.getFirst());
        }

        super.generateLayout(layoutData, renderData);
    }

    public void toggle() {
        this.open = !this.open;
        this.regenerateLayout();
    }
}
