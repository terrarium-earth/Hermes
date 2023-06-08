package earth.terrarium.hermes.api.defaults.lists;

import earth.terrarium.hermes.api.TagElement;
import earth.terrarium.hermes.api.themes.Theme;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

import java.util.Map;

public class OrderedListTagElement extends ListTagElement {

    public OrderedListTagElement(Map<String, String> parameters) {
        super(parameters);
    }

    @Override
    public void render(Theme theme, GuiGraphics graphics, int x, int y, int width, int mouseX, int mouseY, boolean hovered, float partialTicks) {
        Font font = Minecraft.getInstance().font;
        int i = 1;
        int height = 0;
        for (TagElement element : this.children) {
            int indexWidth = font.width(i + ".");
            graphics.drawString(
                font,
                i + ".", x, y + height, this.color.getValue(),
                false
            );
            element.render(theme, graphics, x + indexWidth + 2, y + height, width - indexWidth - 2, mouseX, mouseY, hovered, partialTicks);
            height += Math.max(element.getHeight(width - indexWidth - 2), Minecraft.getInstance().font.lineHeight + 1);
            i++;
        }
    }

    @Override
    int getItemHeight(int index, TagElement element, int width) {
        int indexWidth = Minecraft.getInstance().font.width((index + 1) + ".");
        return Math.max(element.getHeight(width - indexWidth - 2), Minecraft.getInstance().font.lineHeight + 1);
    }
}
