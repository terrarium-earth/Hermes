package earth.terrarium.hermes.api.defaults;

import com.teamresourceful.resourcefullib.client.CloseablePoseStack;
import com.teamresourceful.resourcefullib.common.color.Color;
import earth.terrarium.hermes.api.Alignment;
import earth.terrarium.hermes.api.themes.Theme;
import earth.terrarium.hermes.utils.ElementParsingUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.FormattedCharSequence;

import java.util.Arrays;
import java.util.Map;

public abstract class HeadingTagElement extends TextTagElement {

    public final int scale;

    public HeadingTagElement(Map<String, String> parameters, int scale) {
        super(parameters);
        this.scale = scale;
        this.shadowed = ElementParsingUtils.parseBoolean(parameters, "shadowed", false);
    }

    @Override
    public void render(Theme theme, GuiGraphics graphics, int x, int y, int width, int mouseX, int mouseY, boolean hovered, float partialTicks) {
        try (var ignored = new CloseablePoseStack(graphics)) {
            graphics.pose().scale(scale, scale, scale);
            float translationFactor = (float) (scale - 1) / scale;
            graphics.pose().translate(-x * translationFactor, -y * translationFactor, 0);

            var font = Minecraft.getInstance().font;
            var lines = font.split(component, (width - (10 + (2 * xSurround))) / scale);

            var contentWidth = lines.stream().mapToInt((line) -> font.width(line) - 1).max().orElse(0);
            // from top of top row capitals, to bottom of bottom row letters with descenders (eg: "y")
            int contentHeight = (lines.size() * font.lineHeight) + (lines.size() - 2);
            int[] lineOffsets = lines.stream().mapToInt((line) -> getOffsetForTextTag(width, line)).toArray();
            int contentOffset = Arrays.stream(lineOffsets).min().orElse(width);

            drawFillAndBorder(graphics, x + xSurround + contentOffset, y + ySurround, contentWidth, contentHeight);

            int lineHeight = font.lineHeight;
            for (int i = 0; i < lines.size(); i++) {
                theme.drawText(
                        graphics,
                        lines.get(i),
                        x + xSurround + lineOffsets[i],
                        y + ySurround + (i * (lineHeight + 1)),
                        Color.DEFAULT,
                        this.shadowed
                );
            }
        }
    }

    @Override
    public int getHeight(int width) {
        Font font = Minecraft.getInstance().font;
        int lines = font.split(component, (width - (10 + (2 * xSurround))) / scale).size();
        int lineHeight = font.lineHeight;
        // scale * (element height + vertical spacing)
        return scale * (((lines * lineHeight) + (lines - 2)) + (2 * ySurround));
    }

    @Override
    public int getOffsetForTextTag(int width, FormattedCharSequence text) {
        int textWidth = ((Minecraft.getInstance().font.width(text) - 1) + (2 * xSurround)); // -1 to trim trailing empty space
        float scaledTextWidth = scale * textWidth;
        return Math.round((float) Alignment.getOffset(width, scaledTextWidth, align) / scale);
    }

}
