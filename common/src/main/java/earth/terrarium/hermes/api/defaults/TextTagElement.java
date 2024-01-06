package earth.terrarium.hermes.api.defaults;

import com.teamresourceful.resourcefullib.common.color.Color;
import earth.terrarium.hermes.api.Alignment;
import earth.terrarium.hermes.api.TagElement;
import earth.terrarium.hermes.utils.ElementParsingUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public abstract class TextTagElement extends FillAndBorderElement implements TagElement {

    protected String content = "";
    protected @Nullable Boolean bold;
    protected @Nullable Boolean italic;
    protected @Nullable Boolean underline;
    protected @Nullable Boolean strikethrough;
    protected @Nullable Boolean obfuscated;
    @Deprecated protected boolean centered;
    protected Alignment align;
    protected boolean shadowed;
    protected Color color;

    protected TextTagElement(Map<String, String> parameters) {
        super(parameters);
        this.bold = parameters.containsKey("bold") ? Boolean.parseBoolean(parameters.get("bold")) : null;
        this.italic = parameters.containsKey("italic") ? Boolean.parseBoolean(parameters.get("italic")) : null;
        this.underline = parameters.containsKey("underline") ? Boolean.parseBoolean(parameters.get("underline")) : null;
        this.strikethrough = parameters.containsKey("strikethrough") ? Boolean.parseBoolean(parameters.get("strikethrough")) : null;
        this.obfuscated = parameters.containsKey("obfuscated") ? Boolean.parseBoolean(parameters.get("obfuscated")) : null;
        // 'centered' is DEPRECATED
        this.centered = parameters.containsKey("centered") && Boolean.parseBoolean(parameters.get("centered"));
        this.shadowed = parameters.containsKey("shadowed") && Boolean.parseBoolean(parameters.get("shadowed"));
        this.align = ElementParsingUtils.parseAlignment(parameters, "align", Alignment.MIN);
        if (parameters.containsKey("color")) {
            try {
                this.color = Color.parse(parameters.get("color"));
            } catch (Exception ignored) {
                this.color = Color.DEFAULT;
            }
        } else {
            this.color = Color.DEFAULT;
        }
        // 'centered' is now DEPRECATED; for now, 'centered="true"' parameter over-rides the align parameter
        if (this.centered) {
            align = Alignment.MIDDLE;
        }
    }

    @Override
    public void setContent(String content) {
        this.content = content;
    }

    public int getOffsetForTextTag(int width, FormattedCharSequence text) {
        int textWidth = Minecraft.getInstance().font.width(text) - 1; // -1 to trim trailing empty space
        return Alignment.getOffset(width, textWidth + (2 * hSpacing), align);
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
