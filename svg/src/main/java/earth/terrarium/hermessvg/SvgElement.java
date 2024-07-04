package earth.terrarium.hermessvg;

import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.elements.creators.ElementCreator;
import earth.terrarium.hermes.elements.base.NoOpElement;
import earth.terrarium.hermes.renderer.HermesRenderer;
import earth.terrarium.hermes.styles.HermesStyle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

public class SvgElement extends NoOpElement<HermesStyle, HermesRenderer> {

    public static final ElementCreator<HermesStyle, HermesRenderer> CREATOR = new Creator();

    public SvgElement(@NotNull HermesStyle style, @NotNull LayoutStyle layoutStyle, @Nullable Element<HermesStyle, HermesRenderer> parent, @NotNull String qName, @Nullable Attributes attributes) {
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
        for (Element<HermesStyle, HermesRenderer> child : children) {
            if (!(child instanceof SvgElement element)) {
                throw new IllegalStateException("SvgElement can only contain SvgElement children");
            }
            builder.append(element.toElementString());
        }
        builder.append("</");
        builder.append(qName);
        builder.append(">");
        return builder.toString();
    }

    private static class Creator implements ElementCreator<HermesStyle, HermesRenderer> {

        @Override
        public Element<HermesStyle, HermesRenderer> createElement(HermesStyle style, LayoutStyle layoutStyle, @NotNull Element<HermesStyle, HermesRenderer> element, @NotNull String s1, @NotNull Attributes attributes) {
            return new SvgElement(style, layoutStyle, element, s1, attributes);
        }

        @Override
        public boolean appliesTo(HermesStyle style, LayoutStyle layoutStyle, @NotNull Element<HermesStyle, HermesRenderer> parent, @NotNull String qName, @NotNull Attributes attributes) {
            return parent instanceof Svg || parent instanceof SvgElement;
        }
    }
}
