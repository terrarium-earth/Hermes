package earth.terrarium.hermes.elements.html;

import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.elements.formatting.FormattingElement;
import dev.dediamondpro.minemark.style.Style;
import dev.dediamondpro.minemark.utils.ColorFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

public class FontElement<S extends Style, R> implements FormattingElement<S, R> {

    @Override
    public void applyStyle(@NotNull S style, @NotNull LayoutStyle layoutStyle, @Nullable Element<S, R> parent, @NotNull String qName, @NotNull Attributes attributes) {
        var size = attributes.getValue("size");
        if (size != null) {
            if (size.startsWith("+")) {
                float newSize = Math.min(layoutStyle.getFontSize() + getSize(size.substring(1)), 3.0f);
                layoutStyle.setFontSize(newSize);
            } else if (size.startsWith("-")) {
                float newSize = Math.max(layoutStyle.getFontSize() - getSize(size.substring(1)), 0.5f);
                layoutStyle.setFontSize(newSize);
            } else {
                layoutStyle.setFontSize(getSize(size));
            }
        }

        var color = attributes.getValue("color");
        if (color != null) {
            layoutStyle.setTextColor(ColorFactory.web(color));
        }
    }

    private static float getSize(String size) {
        return switch (size) {
            case "1" -> 0.5f;
            case "2" -> 0.75f;
            case "3" -> 1.0f; // DEFAULT
            case "4" -> 1.25f;
            case "5" -> 1.5f;
            case "6" -> 2.0f;
            case "7" -> 3.0f;
            default -> 1.0f;
        };
    }

    @Override
    public boolean appliesTo(S style, LayoutStyle layoutStyle, @NotNull Element<S, R> parent, @NotNull String qName, @NotNull Attributes attributes) {
        return qName.equals("font");
    }
}
