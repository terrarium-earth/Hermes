package earth.terrarium.hermes.elements.html;

import com.teamresourceful.resourcefullib.client.screens.CursorScreen;
import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.elements.formatting.FormattingElement;
import dev.dediamondpro.minemark.style.Style;
import dev.dediamondpro.minemark.utils.StyleType;
import earth.terrarium.hermes.css.Border;
import earth.terrarium.hermes.css.FontFamily;
import earth.terrarium.hermes.css.VerticalAlignment;
import earth.terrarium.hermes.utils.CssParser;
import net.minecraft.Util;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class GlobalAttributesElement<S extends Style, R> implements FormattingElement<S, R> {

    public static final StyleType<FontFamily> FONT_FAMILY = new StyleType<>("hermes:font_family", FontFamily.class);

    public static final StyleType<Color> BACKGROUND_COLOR = new StyleType<>("hermes:background_color", Color.class);
    public static final StyleType<Border> BORDER = new StyleType<>("hermes:border", Border.class);
    public static final StyleType<Float> PADDING = new StyleType<>("hermes:padding", Float.class);
    public static final StyleType<Float> MARGIN = new StyleType<>("hermes:margin", Float.class);
    public static final StyleType<String> TITLE = new StyleType<>("hermes:title", String.class);
    public static final StyleType<VerticalAlignment> VERTICAL_ALIGNMENT = new StyleType<>("hermes:vertical_alignment", VerticalAlignment.class);
    public static final StyleType<CursorScreen.Cursor> CURSOR = new StyleType<>("hermes:cursor", CursorScreen.Cursor.class);

    private static final Map<String, Consumer<LayoutStyle>> DEFAULT_STYLE = Util.make(new HashMap<>(), map -> {
        map.put("big", style -> style.setFontSize(1.2f));
        map.put("small", style -> style.setFontSize(0.8f));

        map.put("strong", style -> style.setBold(true));
        map.put("b", style -> style.setBold(true));

        map.put("address", style -> style.setItalic(true));
        map.put("cite", style -> style.setItalic(true));
        map.put("var", style -> style.setItalic(true));
        map.put("dfn", style -> style.setItalic(true));
        map.put("em", style -> style.setItalic(true));
        map.put("i", style -> style.setItalic(true));

        map.put("ins", style -> style.setUnderlined(true));
        map.put("u", style -> style.setUnderlined(true));

        map.put("strike", style -> style.setStrikethrough(true));
        map.put("del", style -> style.setStrikethrough(true));
        map.put("s", style -> style.setStrikethrough(true));

        map.put("pre", style -> style.setPreFormatted(true));
    });

    @Override
    public void applyStyle(@NotNull S style, @NotNull LayoutStyle layoutStyle, @Nullable Element<S, R> parent, @NotNull String qName, @NotNull Attributes attributes) {
        DEFAULT_STYLE.getOrDefault(qName, s -> {}).accept(layoutStyle);

        Applicator applicator = new Applicator(layoutStyle);

        applicator.put(TITLE, attributes.getValue("title"));

        var css = CssParser.parseInlineCss(attributes.getValue("style"));

        applicator.put(BACKGROUND_COLOR, CssParser.parseBackgroundColor(css.get("background-color"), qName));
        applicator.put(CURSOR, CssParser.parseCursor(css.get("cursor"), qName));
        applicator.put(PADDING, CssParser.parseUnit(css.get("padding")));
        applicator.put(MARGIN, CssParser.parseUnit(css.get("margin")));

        applicator.put(BORDER, Border.fromCss(css));
        applicator.set(FONT_FAMILY, FontFamily.fromCss(css, qName));
        applicator.set(VERTICAL_ALIGNMENT, VerticalAlignment.fromCss(css, qName));
    }

    @Override
    public boolean appliesTo(S style, LayoutStyle layoutStyle, @NotNull Element<S, R> parent, @NotNull String qName, @NotNull Attributes attributes) {
        return true;
    }

    public record Applicator(LayoutStyle style) {

        /**
         * Adds to the layout, this will propagate to its children
         */
        public <T> void set(StyleType<T> type, T value) {
            if (value == null) return;
            style.put(type, value);
        }

        /**
         * Adds to the layout, this will not propagate to its children
         */
        public <T> void put(StyleType<T> type, T value) {
            if (value != null) {
                style.put(type, value);
            } else {
                style.remove(type);
            }
        }
    }
}
