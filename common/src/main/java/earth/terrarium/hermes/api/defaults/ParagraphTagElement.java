package earth.terrarium.hermes.api.defaults;

import earth.terrarium.hermes.api.themes.Theme;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.Map;
import java.util.Arrays;

public class ParagraphTagElement extends TextTagElement {

    public ParagraphTagElement(Map<String, String> parameters) {
        super(parameters);
    }

    @Override
    public void render(Theme theme, GuiGraphics graphics, int x, int y, int width, int mouseX, int mouseY, boolean hovered, float partialTicks) {

        Component text = Component.nullToEmpty(this.content).copy().setStyle(this.getStyle());
        var font = Minecraft.getInstance().font;
        var lines = font.split(text, width - (5 + (2 * xMargin)));

        var contentWidth = lines.stream().mapToInt((line) -> font.width(line) - 1).max().orElse(0);
        // from top of top row capitals, to bottom of bottom row letters with descenders (eg: "y")
        int contentHeight = (lines.size() * font.lineHeight) + (lines.size() - 2);
        int[] lineOffsets = lines.stream().mapToInt((line) -> getOffsetForTextTag(width, line)).toArray();
        int contentOffset = Arrays.stream(lineOffsets).min().orElse(width);

        drawFillAndBorder(graphics, x + xMargin + contentOffset, y + yMargin, contentWidth, contentHeight);

        int lineHeight = font.lineHeight;
        for (int i = 0; i < lines.size(); i++) {
            theme.drawText(
                    graphics,
                    lines.get(i),
                    x + xMargin + lineOffsets[i],
                    y + yMargin + (i * (lineHeight + 1)),
                    this.color,
                    this.shadowed
            );
        }
    }

    @Override
    public int getHeight(int width) {
        int lines = Minecraft.getInstance().font.split(Component.nullToEmpty(this.content), width - (5 + (2 * xMargin))).size();
        int lineHeight = Minecraft.getInstance().font.lineHeight;
        return ((lines * lineHeight) + (lines - 2)) + (2 * yMargin); // element height + vertical spacing
    }
}
