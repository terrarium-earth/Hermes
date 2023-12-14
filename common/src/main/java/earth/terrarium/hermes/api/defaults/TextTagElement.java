package earth.terrarium.hermes.api.defaults;

import com.teamresourceful.resourcefullib.common.color.Color;
import earth.terrarium.hermes.api.Alignable;
import earth.terrarium.hermes.api.TagElement;
import earth.terrarium.hermes.utils.ElementParsingUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public abstract class TextTagElement implements TagElement, Alignable {

    protected String content = "";
    protected @Nullable Boolean bold;
    protected @Nullable Boolean italic;
    protected @Nullable Boolean underline;
    protected @Nullable Boolean strikethrough;
    protected @Nullable Boolean obfuscated;
    protected boolean centered;
    protected boolean shadowed;
    protected Color color;
    protected final Alignment align;

    protected TextTagElement(Map<String, String> parameters) {
        this.bold = parameters.containsKey("bold") ? Boolean.parseBoolean(parameters.get("bold")) : null;
        this.italic = parameters.containsKey("italic") ? Boolean.parseBoolean(parameters.get("italic")) : null;
        this.underline = parameters.containsKey("underline") ? Boolean.parseBoolean(parameters.get("underline")) : null;
        this.strikethrough = parameters.containsKey("strikethrough") ? Boolean.parseBoolean(parameters.get("strikethrough")) : null;
        this.obfuscated = parameters.containsKey("obfuscated") ? Boolean.parseBoolean(parameters.get("obfuscated")) : null;
        this.centered = parameters.containsKey("centered") && Boolean.parseBoolean(parameters.get("centered"));
        this.shadowed = parameters.containsKey("shadowed") && Boolean.parseBoolean(parameters.get("shadowed"));
        this.align = ElementParsingUtils.parseAlignment(parameters, "align", Alignment.LEFT);
        if (parameters.containsKey("color")) {
            try {
                this.color = Color.parse(parameters.get("color"));
            } catch (Exception ignored) {
                this.color = Color.DEFAULT;
            }
        } else {
            this.color = Color.DEFAULT;
        }
    }

    @Override
    public void setContent(String content) {
        this.content = content;
    }

    public int getXOffset(int x, int width, FormattedCharSequence text) {
        return switch (align) {
            case LEFT -> x;
            case CENTER -> x + (int) (((width - Minecraft.getInstance().font.width(text)) / 2f) + 0.5f);
            case RIGHT -> x + width - Minecraft.getInstance().font.width(text);
        };
    }

    public Style getStyle() {
        return Style.EMPTY
                .withBold(bold)
                .withItalic(italic)
                .withUnderlined(underline)
                .withStrikethrough(strikethrough)
                .withObfuscated(obfuscated);
    }
}
