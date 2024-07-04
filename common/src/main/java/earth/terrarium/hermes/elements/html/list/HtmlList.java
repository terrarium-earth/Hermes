package earth.terrarium.hermes.elements.html.list;

import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.elements.impl.list.ListHolderElement;
import earth.terrarium.hermes.api.rendering.HtmlRenderer;
import earth.terrarium.hermes.api.rendering.HtmlStyle;
import earth.terrarium.hermes.utils.AttributeParser;
import earth.terrarium.hermes.utils.CssParser;
import earth.terrarium.hermes.utils.Numerals;
import net.minecraft.Optionull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

public class HtmlList extends ListHolderElement<HtmlStyle, HtmlRenderer> {

    protected final Style style;
    protected final int start;

    public HtmlList(@NotNull HtmlStyle style, @NotNull LayoutStyle layoutStyle, @Nullable Element<HtmlStyle, HtmlRenderer> parent, @NotNull String qName, @NotNull Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);
        assert attributes != null;

        if (qName.equals("dl")) {
            this.style = Style.NONE;
            this.start = 1;
        } else {
            var css = CssParser.parseInlineCss(attributes.getValue("style"));
            var styleType = Optionull.map(css.get("list-style-type"), String::intern);
            this.style = switch (listType) {
                case ORDERED -> switch (styleType) {
                    case "upper-roman" -> Style.UPPER_ROMAN;
                    case "lower-roman" -> Style.LOWER_ROMAN;
                    case "upper-alpha" -> Style.UPPER_ALPHA;
                    case "lower-alpha" -> Style.LOWER_ALPHA;
                    case "none" -> Style.NONE;
                    case null, default -> Style.NUMBER;
                };
                case UNORDERED -> switch (styleType) {
                    case "circle" -> Style.CIRCLE;
                    case "square" -> Style.SQUARE;
                    case null, default -> Style.DISC;
                };
            };

            this.start = AttributeParser.parseInt(attributes, "start", 0);
        }
    }

    public Style getStyle() {
        return style;
    }

    public int getStart() {
        return start;
    }

    public enum Style {
        NONE,

        LOWER_ROMAN,
        UPPER_ROMAN,
        LOWER_ALPHA,
        UPPER_ALPHA,
        NUMBER,

        DISC,
        CIRCLE,
        SQUARE,
        ;

        public String create(int index) {
            return switch (this) {
                case LOWER_ROMAN -> Numerals.toRoman(index, Numerals.Casing.LOWER) + ". ";
                case UPPER_ROMAN -> Numerals.toRoman(index, Numerals.Casing.UPPER) + ". ";
                case LOWER_ALPHA -> Numerals.toAlpha(index, Numerals.Casing.LOWER) + ". ";
                case UPPER_ALPHA -> Numerals.toAlpha(index, Numerals.Casing.UPPER) + ". ";
                case NUMBER -> index + ". ";
                case DISC -> "⏺ ";
                case CIRCLE -> "○ ";
                case SQUARE -> "■ ";
                case NONE -> "";
            };
        }
    }
}
