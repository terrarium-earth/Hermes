package earth.terrarium.hermes.api.rendering;

import com.teamresourceful.resourcefullib.client.screens.CursorScreen;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector4f;

public interface HtmlRenderer {

    float width(String text, float scale, boolean monospaced);

    default float width(String text, float scale) {
        return width(text, scale, false);
    }

    void drawString(String text, float x, float y, float scale, int color, boolean shadow, boolean monospaced);

    default void drawString(String text, float x, float y, float scale, int color, boolean shadow) {
        drawString(text, x, y, scale, color, shadow, false);
    }

    void fill(float x, float y, float width, float height, int backgroundColor, int borderColor, float borderWidth, @Nullable Vector4f borderRadius);

    default void fill(float x, float y, float width, float height, int color) {
        fill(x, y, width, height, color, color, 0, null);
    }

    void blit(
            ResourceLocation texture,
            float x, float y, float u0, float v0, float u1, float v1,
            float width, float height,
            Vector4f borderRadius
    );

    void blit(
            int texture,
            float x, float y, float u0, float v0, float u1, float v1,
            float width, float height
    );

    default void blit(ResourceLocation texture, float x, float y, float width, float height, @Nullable Vector4f borderRadius) {
        blit(texture, x, y, 0f, 0f, 1f, 1f, width, height, borderRadius);
    }

    default void blit(int texture, float x, float y, float width, float height) {
        blit(texture, x, y, 0f, 0f, 1f, 1f, width, height);
    }

    default void blit(ResourceLocation texture, float x, float y, float width, float height) {
        blit(texture, x, y, 0f, 0f, 1f, 1f, width, height, null);
    }

    Font getFont();

    Font getMonospacedFont();

    GuiGraphics getGraphics();

    void setTooltip(Component component);

    Component getTooltip();

    void setCursor(CursorScreen.Cursor cursor);
}
