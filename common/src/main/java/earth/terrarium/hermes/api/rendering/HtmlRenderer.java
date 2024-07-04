package earth.terrarium.hermes.api.rendering;

import com.teamresourceful.resourcefullib.client.screens.CursorScreen;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public interface HtmlRenderer {

    float width(String text, float scale);

    void drawString(String text, float x, float y, float scale, int color, boolean shadow);

    void fill(float x, float y, float width, float height, int color);

    void blit(ResourceLocation texture, float x, float y, float width, float height);

    Font getFont();

    GuiGraphics getGraphics();

    void setTooltip(Component component);

    Component getTooltip();

    void setCursor(CursorScreen.Cursor cursor);
}
