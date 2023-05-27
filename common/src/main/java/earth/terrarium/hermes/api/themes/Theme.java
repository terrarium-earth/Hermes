package earth.terrarium.hermes.api.themes;

import net.minecraft.client.gui.GuiGraphics;

public interface Theme {

    void drawDropdown(GuiGraphics graphics, int x, int y, int width, boolean hovered, boolean open, String text);

    void drawSlot(GuiGraphics graphics, int x, int y, boolean hovered);

    void drawArrow(GuiGraphics graphics, int x, int y);

    void drawCraftingBackground(GuiGraphics graphics, int x, int y, int width, int height);

    void drawCarouselButton(GuiGraphics graphics, int x, int y, boolean left, boolean hovered);

}
