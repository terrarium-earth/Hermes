package earth.terrarium.hermes.api.defaults;

import earth.terrarium.hermes.api.themes.Theme;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.Map;

public class ParagraphTagElement extends TextTagElement {

    public ParagraphTagElement(Map<String, String> parameters) {
        super(parameters);
    }

    @Override
    public void render(Theme theme, GuiGraphics graphics, int x, int y, int width, int mouseX, int mouseY, boolean hovered, float partialTicks) {
        Component text = Component.nullToEmpty(this.content).copy().setStyle(this.getStyle());
        int height = 0;
        for (FormattedCharSequence sequence : Minecraft.getInstance().font.split(text, width - 5)) {
            theme.drawText(
                graphics,
                sequence,
                x + getOffsetForTextTag(width, sequence),
                y + height,
                this.color,
                this.shadowed
            );
            height += Minecraft.getInstance().font.lineHeight + 1;
        }
    }

    @Override
    public int getHeight(int width) {
        int lines = Minecraft.getInstance().font.split(Component.nullToEmpty(this.content), width - 5).size();
        return lines * (Minecraft.getInstance().font.lineHeight + 1);
    }
}
