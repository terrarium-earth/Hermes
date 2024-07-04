package earth.terrarium.hermes.elements.html;

import com.teamresourceful.resourcefullib.client.screens.CursorScreen;
import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.elements.formatting.FormattingElement;
import dev.dediamondpro.minemark.style.Style;
import dev.dediamondpro.minemark.utils.ColorFactory;
import dev.dediamondpro.minemark.utils.StyleType;
import earth.terrarium.hermes.utils.CssParser;
import earth.terrarium.hermes.utils.types.VerticalAlignment;
import net.minecraft.Optionull;
import net.minecraft.Util;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class GlobalAttributesElement<S extends Style, R> implements FormattingElement<S, R> {

    public static final StyleType<Color> BACKGROUND_COLOR = new StyleType<>("hermes:background_color", Color.class);
    public static final StyleType<Color> BORDER_COLOR = new StyleType<>("hermes:border_color", Color.class);
    public static final StyleType<String> TITLE = new StyleType<>("hermes:title", String.class);
    public static final StyleType<VerticalAlignment> VERTICAL_ALIGNMENT = new StyleType<>("hermes:vertical_alignment", VerticalAlignment.class);
    public static final StyleType<CursorScreen.Cursor> CURSOR = new StyleType<>("hermes:cursor", CursorScreen.Cursor.class);

    private static final Map<String, Consumer<LayoutStyle>> DEFAULT_STYLE = Util.make(new HashMap<>(), map -> {
        map.put("small", style -> style.setFontSize(0.8f));

        map.put("strong", style -> style.setBold(true));
        map.put("b", style -> style.setBold(true));

        map.put("address", style -> style.setItalic(true));
        map.put("cite", style -> style.setItalic(true));
        map.put("var", style -> style.setItalic(true));
        map.put("dfn", style -> style.setItalic(true));
        map.put("em", style -> style.setItalic(true));
        map.put("q", style -> style.setItalic(true));
        map.put("i", style -> style.setItalic(true));

        map.put("ins", style -> style.setUnderlined(true));
        map.put("u", style -> style.setUnderlined(true));

        map.put("del", style -> style.setStrikethrough(true));
        map.put("s", style -> style.setStrikethrough(true));

        map.put("pre", style -> style.setPreFormatted(true));
    });

    @Override
    public void applyStyle(@NotNull S style, @NotNull LayoutStyle layoutStyle, @Nullable Element<S, R> parent, @NotNull String qName, @NotNull Attributes attributes) {
        layoutStyle.put(TITLE, attributes.getValue("title"));

        var css = CssParser.parseInlineCss(attributes.getValue("style"));

        layoutStyle.put(BACKGROUND_COLOR, CssParser.parseBackgroundColor(css.get("background-color"), qName));
        layoutStyle.put(BORDER_COLOR, Optionull.map(css.get("border-color"), ColorFactory::web));
        layoutStyle.put(VERTICAL_ALIGNMENT, VerticalAlignment.from(css.get("vertical-align"), qName));
        layoutStyle.put(CURSOR, CssParser.parseCursor(css.get("cursor"), qName));

        DEFAULT_STYLE.getOrDefault(qName, s -> {}).accept(layoutStyle);
    }

    @Override
    public boolean appliesTo(S style, LayoutStyle layoutStyle, @NotNull Element<S, R> parent, @NotNull String qName, @NotNull Attributes attributes) {
        return true;
    }
}
