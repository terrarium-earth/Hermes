package earth.terrarium.hermes.api.defaults;

import com.teamresourceful.resourcefullib.client.CloseablePoseStack;
import earth.terrarium.hermes.api.Alignment;
import earth.terrarium.hermes.api.themes.Theme;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.Arrays;
import java.util.Map;

public abstract class HeadingTagElement extends TextTagElement {

    public final int scale;

    public HeadingTagElement(Map<String, String> parameters, int scale) {
        super(parameters);
        this.scale = scale;
    }

    @Override
    public void render(Theme theme, GuiGraphics graphics, int x, int y, int width, int mouseX, int mouseY, boolean hovered, float partialTicks) {
        try (var ignored = new CloseablePoseStack(graphics)) {
            graphics.pose().scale(scale, scale, scale);
            float translationFactor = (float) (scale - 1) / scale;
            graphics.pose().translate(-x * translationFactor, -y * translationFactor, 0);

            Component text = Component.nullToEmpty(this.content).copy().setStyle(this.getStyle());
            var font = Minecraft.getInstance().font;
            var lines = font.split(text, (width - (10 + (2 * xMargin))) / scale);

            var contentWidth = lines.stream().mapToInt((line) -> font.width(line) - 1).max().orElse(0);
            // from top of top row capitals, to bottom of bottom row letters with descenders (eg: "y")
            int contentHeight = (lines.size() * font.lineHeight) + (lines.size() - 2);
            int[] lineOffsets = lines.stream().mapToInt((line) -> getOffsetForTextTag(width, line)).toArray();
            int contentOffset = Arrays.stream(lineOffsets).min().orElse(width);

            drawBackground(graphics, x + xMargin + contentOffset, y + yMargin, contentWidth, contentHeight);;

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
    }

    @Override
    public int getHeight(int width) {
        int lines = Minecraft.getInstance().font.split(Component.nullToEmpty(this.content), (width - (10 + (2 * xMargin))) / scale).size();
        int lineHeight = Minecraft.getInstance().font.lineHeight;
        // scale * (element height + vertical spacing)
        return scale * (((lines * lineHeight) + (lines - 2)) + (2 * yMargin));
    }

    @Override
    public int getOffsetForTextTag(int width, FormattedCharSequence text) {
        int textWidth = ((Minecraft.getInstance().font.width(text) - 1) + (2 * xMargin)); // -1 to trim trailing empty space
        float scaledTextWidth = scale * textWidth;
        return Math.round((float) Alignment.getOffset(width, scaledTextWidth, align) / scale);
    }

}
