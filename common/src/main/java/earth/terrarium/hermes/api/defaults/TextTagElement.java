package earth.terrarium.hermes.api.defaults;

import com.teamresourceful.resourcefullib.common.color.Color;
import earth.terrarium.hermes.api.TagElement;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public abstract class TextTagElement implements TagElement {

    protected String content = "";
    protected @Nullable Boolean bold;
    protected @Nullable Boolean italic;
    protected @Nullable Boolean underline;
    protected @Nullable Boolean strikethrough;
    protected @Nullable Boolean obfuscated;
    protected boolean centered;
    protected boolean shadowed;
    protected Color color;

    protected TextTagElement(Map<String, String> parameters) {
        this.bold = parameters.containsKey("bold") ? Boolean.parseBoolean(parameters.get("bold")) : null;
        this.italic = parameters.containsKey("italic") ? Boolean.parseBoolean(parameters.get("italic")) : null;
        this.underline = parameters.containsKey("underline") ? Boolean.parseBoolean(parameters.get("underline")) : null;
        this.strikethrough = parameters.containsKey("strikethrough") ? Boolean.parseBoolean(parameters.get("strikethrough")) : null;
        this.obfuscated = parameters.containsKey("obfuscated") ? Boolean.parseBoolean(parameters.get("obfuscated")) : null;
        this.centered = parameters.containsKey("centered") && Boolean.parseBoolean(parameters.get("centered"));
        this.shadowed = parameters.containsKey("shadowed") && Boolean.parseBoolean(parameters.get("shadowed"));
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
        return Boolean.TRUE.equals(this.centered) ? x + (width - Minecraft.getInstance().font.width(text)) / 2 : x;
    }

    public int getXOffset(int x, int width, int scale, FormattedCharSequence text) {
        int textWidth = scale * Minecraft.getInstance().font.width(text);
        return Boolean.TRUE.equals(this.centered) ? x + (int) ((((width - textWidth) / 2f) / scale) + 0.5f) : x;
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
