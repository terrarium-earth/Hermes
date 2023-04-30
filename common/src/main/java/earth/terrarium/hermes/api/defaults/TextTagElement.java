package earth.terrarium.hermes.api.defaults;

import com.teamresourceful.resourcefullib.common.color.Color;
import earth.terrarium.hermes.api.TagElement;
import net.minecraft.client.Minecraft;
import net.minecraft.util.FormattedCharSequence;

import java.util.Map;

public abstract class TextTagElement implements TagElement {

    protected String content = "";
    protected boolean bold;
    protected boolean italic;
    protected boolean underline;
    protected boolean strikethrough;
    protected boolean obfuscated;
    protected boolean centered;
    protected boolean shadowed;
    protected Color color;

    protected TextTagElement(Map<String, String> parameters) {
        this.bold = parameters.containsKey("bold") && Boolean.parseBoolean(parameters.get("bold"));
        this.italic = parameters.containsKey("italic") && Boolean.parseBoolean(parameters.get("italic"));
        this.underline = parameters.containsKey("underline") && Boolean.parseBoolean(parameters.get("underline"));
        this.strikethrough = parameters.containsKey("strikethrough") && Boolean.parseBoolean(parameters.get("strikethrough"));
        this.obfuscated = parameters.containsKey("obfuscated") && Boolean.parseBoolean(parameters.get("obfuscated"));
        this.centered = parameters.containsKey("centered") && Boolean.parseBoolean(parameters.get("centered"));
        this.shadowed = parameters.containsKey("shadowed") && Boolean.parseBoolean(parameters.get("shadowed"));
        if (parameters.containsKey("color")) {
            try {
                this.color = Color.parse(parameters.get("color"));
            }catch (Exception e) {
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
        return this.centered ? x + (width - Minecraft.getInstance().font.width(text)) / 2 : x;
    }
}
