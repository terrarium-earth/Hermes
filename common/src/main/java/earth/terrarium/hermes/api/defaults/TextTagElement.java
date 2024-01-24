package earth.terrarium.hermes.api.defaults;

import com.teamresourceful.resourcefullib.common.color.Color;
import earth.terrarium.hermes.api.Alignment;
import earth.terrarium.hermes.api.TagElement;
import earth.terrarium.hermes.api.TagProvider;
import earth.terrarium.hermes.api.text.ChildTextTagElement;
import earth.terrarium.hermes.api.text.TextTagProvider;
import earth.terrarium.hermes.api.themes.Theme;
import earth.terrarium.hermes.utils.ElementParsingUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;

import java.util.List;
import java.util.Map;

public class TextTagElement extends FillAndBorderElement implements TagElement {

    protected MutableComponent component = Component.empty();
    protected Alignment align;
    protected boolean shadowed;

    public TextTagElement(Map<String, String> parameters) {
        super(parameters);

        this.align = ElementParsingUtils.parseAlignment(parameters, "align", Alignment.MIN);
        this.shadowed = ElementParsingUtils.parseBoolean(parameters, "shadowed", false);
    }

    @Override
    public void render(Theme theme, GuiGraphics graphics, int x, int y, int width, int mouseX, int mouseY, boolean hovered, float partialTicks) {
        x = x + xSurround;
        y = y + ySurround;

        Font font = Minecraft.getInstance().font;
        List<FormattedCharSequence> lines = font.split(component, width - 5);
        int maxWidth = lines.stream().mapToInt(font::width).max().orElse(0);
        int maxHeight = lines.size() * (font.lineHeight + 1) + (lines.size() - 2);
        int offsetX = Alignment.getOffset(width, maxWidth + (2 * xSurround), align);

        drawFillAndBorder(graphics, x + offsetX, y, maxWidth, maxHeight);

        int actMouseX = mouseX - x;
        int actMouseY = mouseY - y;
        int height = 0;
        for (FormattedCharSequence sequence : font.split(component, width - 5)) {
            int textOffset = getOffsetForTextTag(width, sequence);
            theme.drawText(graphics, sequence, x + textOffset, y + height, Color.DEFAULT, true);

            if (actMouseX >= textOffset && actMouseX <= width && actMouseY >= height && actMouseY <= height + font.lineHeight) {
                graphics.renderComponentHoverEffect(
                    font,
                    font.getSplitter().componentStyleAtWidth(sequence, Mth.floor(actMouseX - textOffset)),
                    mouseX, mouseY
                );
            }

            height += Minecraft.getInstance().font.lineHeight + 1;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button, int width) {
        Font font = Minecraft.getInstance().font;
        int height = 0;
        for (FormattedCharSequence sequence : font.split(component, width - 5)) {
            int textOffset = getOffsetForTextTag(width, sequence);
            if (mouseX >= textOffset && mouseX <= width && mouseY >= height && mouseY <= height + font.lineHeight) {
                Style style = font.getSplitter().componentStyleAtWidth(sequence, Mth.floor(mouseX - textOffset));
                if (Minecraft.getInstance().screen != null) {
                    Minecraft.getInstance().screen.handleComponentClicked(style);
                }
                return true;
            }
            height += Minecraft.getInstance().font.lineHeight + 1;
        }
        return false;
    }

    @Override
    public int getHeight(int width) {
        Font font = Minecraft.getInstance().font;
        int lines = font.split(component, width - 5).size();
        return lines * (font.lineHeight + 1);
    }

    @Override
    public void addText(String content) {
        this.component.append(content);
    }

    @Override
    public void addChild(TagElement element) {
        if (element instanceof TextTagElement textTag) {
            this.component.append(textTag.component);
        } else if (element instanceof ChildTextTagElement textTag) {
            this.component.append(textTag.component());
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public TagProvider getChildTagProvider(TagProvider parent) {
        return TextTagProvider.INSTANCE;
    }

    public int getOffsetForTextTag(int width, FormattedCharSequence text) {
        int textWidth = Minecraft.getInstance().font.width(text) - 1; // -1 to trim trailing empty space
        return Alignment.getOffset(width, textWidth + (2 * xSurround), align);
    }
}
