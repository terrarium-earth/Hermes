package earth.terrarium.hermes.api.themes;

import com.teamresourceful.resourcefullib.common.color.Color;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.FormattedCharSequence;

public interface Theme {

    void drawDropdown(GuiGraphics graphics, int x, int y, int width, boolean hovered, boolean open, String text);

    void drawSlot(GuiGraphics graphics, int x, int y, boolean hovered);

    void drawArrow(GuiGraphics graphics, int x, int y);

    void drawCraftingBackground(GuiGraphics graphics, int x, int y, int width, int height);

    void drawCarouselButton(GuiGraphics graphics, int x, int y, boolean left, boolean hovered);

    void drawText(GuiGraphics graphics, FormattedCharSequence text, int x, int y, Color color, boolean shadow);

    void drawText(GuiGraphics graphics, String text, int x, int y, Color color, boolean shadow);

}
