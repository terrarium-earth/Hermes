package earth.terrarium.hermes.api.defaults.lists;

import earth.terrarium.hermes.api.TagElement;
import earth.terrarium.hermes.api.themes.Theme;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

import java.util.Map;

public class UnorderedListTagElement extends ListTagElement {

    public UnorderedListTagElement(Map<String, String> parameters) {
        super(parameters);
    }

    @Override
    public void render(Theme theme, GuiGraphics graphics, int x, int y, int width, int mouseX, int mouseY, boolean hovered, float partialTicks) {
        Font font = Minecraft.getInstance().font;
        int dotWidth = font.width("•");
        int height = 0;
        for (TagElement element : this.children) {
            graphics.drawString(
                font,
                "•", x, y + height, this.color.getValue(),
                false
            );
            element.render(theme, graphics, x + dotWidth + 2, y + height, width - dotWidth - 2, mouseX, mouseY, hovered, partialTicks);
            height += Math.max(element.getHeight(width - dotWidth - 2), Minecraft.getInstance().font.lineHeight + 1);
        }
    }

    @Override
    int getItemHeight(int index, TagElement element, int width) {
        int dotWidth = Minecraft.getInstance().font.width("•");
        return Math.max(element.getHeight(width - dotWidth - 2), Minecraft.getInstance().font.lineHeight + 1);
    }
}
