package earth.terrarium.hermes.api.defaults;

import com.teamresourceful.resourcefullib.client.CloseablePoseStack;
import earth.terrarium.hermes.api.themes.Theme;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.Map;

public class HeadingOneTagElement extends TextTagElement {

    private static final int SCALE = 3;

    public HeadingOneTagElement(Map<String, String> parameters) {
        super(parameters);
    }

    @Override
    public void render(Theme theme, GuiGraphics graphics, int x, int y, int width, int mouseX, int mouseY, boolean hovered, float partialTicks) {
        try (var ignored = new CloseablePoseStack(graphics)) {
            graphics.pose().scale(SCALE, SCALE, SCALE);
            graphics.pose().translate(-x / 1.5f, -y / 1.5f, 0);
            Component text = Component.nullToEmpty(this.content).copy().setStyle(this.getStyle());
            int height = 0;
            for (FormattedCharSequence sequence : Minecraft.getInstance().font.split(text, (width - 10) / SCALE)) {
                theme.drawText(
                    graphics,
                    sequence,
                    getXOffset(x, width, SCALE, sequence), y + height,
                    this.color, this.shadowed
                );
                height += Minecraft.getInstance().font.lineHeight + 1;
            }
        }
    }

    @Override
    public int getHeight(int width) {
        int lines = Minecraft.getInstance().font.split(Component.nullToEmpty(this.content), (width - 10) / SCALE).size();
        return lines * (Minecraft.getInstance().font.lineHeight + 1) * SCALE;
    }
}
