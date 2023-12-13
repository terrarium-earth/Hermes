package earth.terrarium.hermes.api.defaults;

import com.teamresourceful.resourcefullib.client.CloseablePoseStack;
import earth.terrarium.hermes.api.themes.Theme;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

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
            float translationFactor = 1 + (1f / (scale - 1));
            graphics.pose().translate(-x / translationFactor, -y / translationFactor, 0);
            Component text = Component.nullToEmpty(this.content).copy().setStyle(this.getStyle());
            int height = 0;
            for (FormattedCharSequence sequence : Minecraft.getInstance().font.split(text, (width - 10) / scale)) {
                theme.drawText(
                        graphics,
                        sequence,
                        getXOffset(x, width, sequence), y + height,
                        this.color, this.shadowed
                );
                height += Minecraft.getInstance().font.lineHeight + 1;
            }
        }
    }

    @Override
    public int getHeight(int width) {
        int lines = Minecraft.getInstance().font.split(Component.nullToEmpty(this.content), (width - 10) / scale).size();
        return lines * (Minecraft.getInstance().font.lineHeight + 1) * scale;
    }

    @Override
    public int getXOffset(int x, int width, FormattedCharSequence text) {
        int textWidth = scale * Minecraft.getInstance().font.width(text);
        return Boolean.TRUE.equals(this.centered) ? x + (int) ((((width - textWidth) / 2f) / scale) + 0.5f) : x;
    }
}