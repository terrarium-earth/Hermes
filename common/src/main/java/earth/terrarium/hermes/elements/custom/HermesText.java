package earth.terrarium.hermes.elements.custom;

import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.elements.formatting.FormattingElement;
import dev.dediamondpro.minemark.style.Style;
import dev.dediamondpro.minemark.utils.StyleType;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class HermesText<S extends Style, R> implements FormattingElement<S, R> {

    public static final StyleType<Boolean> OBFUSCATED = new StyleType<>("hermes:obfuscated", Boolean.class);

    private static final Map<String, ChatFormatting> FORMATTING = Util.make(new HashMap<>(), map -> {
        map.put("black", ChatFormatting.BLACK);
        map.put("dark_blue", ChatFormatting.DARK_BLUE);
        map.put("dark_aqua", ChatFormatting.DARK_AQUA);
        map.put("dark_red", ChatFormatting.DARK_RED);
        map.put("dark_purple", ChatFormatting.DARK_PURPLE);
        map.put("gold", ChatFormatting.GOLD);
        map.put("gray", ChatFormatting.GRAY);
        map.put("dark_gray", ChatFormatting.DARK_GRAY);
        map.put("blue", ChatFormatting.BLUE);
        map.put("green", ChatFormatting.GREEN);
        map.put("aqua", ChatFormatting.AQUA);
        map.put("red", ChatFormatting.RED);
        map.put("light_purple", ChatFormatting.LIGHT_PURPLE);
        map.put("yellow", ChatFormatting.YELLOW);
        map.put("white", ChatFormatting.WHITE);

        map.put("bold", ChatFormatting.BOLD);

        map.put("italic", ChatFormatting.ITALIC);

        map.put("underline", ChatFormatting.UNDERLINE);

        map.put("strikethrough", ChatFormatting.STRIKETHROUGH);
        map.put("st", ChatFormatting.STRIKETHROUGH);

        map.put("obfuscated", ChatFormatting.OBFUSCATED);
        map.put("obf", ChatFormatting.OBFUSCATED);
    });


    @Override
    public void applyStyle(@NotNull S style, @NotNull LayoutStyle layoutStyle, @Nullable Element<S, R> parent, @NotNull String qName, @NotNull Attributes attributes) {
        ChatFormatting formatting = FORMATTING.get(qName);
        switch (formatting) {
            case BOLD -> layoutStyle.setBold(true);
            case STRIKETHROUGH -> layoutStyle.setStrikethrough(true);
            case UNDERLINE -> layoutStyle.setUnderlined(true);
            case ITALIC -> layoutStyle.setItalic(true);
            case OBFUSCATED -> layoutStyle.put(OBFUSCATED, true);
            case null -> {}
            default -> layoutStyle.setTextColor(new Color(formatting.getColor()));
        }
    }

    @Override
    public boolean appliesTo(S style, LayoutStyle layoutStyle, @NotNull Element<S, R> parent, @NotNull String qName, @NotNull Attributes attributes) {
        return FORMATTING.containsKey(qName);
    }
}
