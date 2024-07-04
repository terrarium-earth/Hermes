package earth.terrarium.hermessvg;

import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.elements.creators.ElementCreator;
import earth.terrarium.hermes.api.rendering.HtmlRenderer;
import earth.terrarium.hermes.api.rendering.HtmlStyle;
import earth.terrarium.hermes.elements.base.NoOpElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

public class SvgEntryElement extends NoOpElement<HtmlStyle, HtmlRenderer> {

    public static final ElementCreator<HtmlStyle, HtmlRenderer> CREATOR = new Creator();

    public SvgEntryElement(@NotNull HtmlStyle style, @NotNull LayoutStyle layoutStyle, @Nullable Element<HtmlStyle, HtmlRenderer> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);
    }

    public String toElementString() {
        StringBuilder builder = new StringBuilder();
        builder.append("<");
        builder.append(qName);
        if (attributes != null) {
            for (int i = 0; i < attributes.getLength(); i++) {
                builder.append(" ");
                builder.append(attributes.getQName(i));
                builder.append("=\"");
                builder.append(attributes.getValue(i));
                builder.append("\"");
            }
        }
        builder.append(">");
        for (Element<HtmlStyle, HtmlRenderer> child : children) {
            if (!(child instanceof SvgEntryElement element)) {
                throw new IllegalStateException("SvgElement can only contain SvgElement children");
            }
            builder.append(element.toElementString());
        }
        builder.append("</");
        builder.append(qName);
        builder.append(">");
        return builder.toString();
    }

    private static class Creator implements ElementCreator<HtmlStyle, HtmlRenderer> {

        @Override
        public Element<HtmlStyle, HtmlRenderer> createElement(HtmlStyle style, LayoutStyle layoutStyle, @NotNull Element<HtmlStyle, HtmlRenderer> element, @NotNull String s1, @NotNull Attributes attributes) {
            return new SvgEntryElement(style, layoutStyle, element, s1, attributes);
        }

        @Override
        public boolean appliesTo(HtmlStyle style, LayoutStyle layoutStyle, @NotNull Element<HtmlStyle, HtmlRenderer> parent, @NotNull String qName, @NotNull Attributes attributes) {
            return parent instanceof SvgElement || parent instanceof SvgEntryElement;
        }
    }
}
