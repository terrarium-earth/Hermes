package earth.terrarium.hermes.api.defaults;

import com.teamresourceful.resourcefullib.client.CloseablePoseStack;
import earth.terrarium.hermes.api.themes.Theme;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.Map;

public class HeadingTwoTagElement extends TextTagElement {

    public HeadingTwoTagElement(Map<String, String> parameters) {
        super(parameters);
    }

    @Override
    public void render(Theme theme, GuiGraphics graphics, int x, int y, int width, int mouseX, int mouseY, boolean hovered, float partialTicks) {
        try (var ignored = new CloseablePoseStack(graphics)) {
            graphics.pose().scale(2, 2, 2);
            graphics.pose().translate(-x / 2f, -y / 2f, 0);
            Component text = Component.nullToEmpty(this.content);
            int height = 0;
            for (FormattedCharSequence sequence : Minecraft.getInstance().font.split(text, width - 10)) {
                graphics.drawString(
                    Minecraft.getInstance().font,
                    sequence, x + 2, y + height, this.color.getValue(),
                    false
                );
                height += Minecraft.getInstance().font.lineHeight + 1;
            }
        }
    }

    @Override
    public int getHeight(int width) {
        int lines = Minecraft.getInstance().font.split(Component.nullToEmpty(this.content), width - 10).size();
        return lines * (Minecraft.getInstance().font.lineHeight + 1) * 2;
    }
}
