package earth.terrarium.hermes.renderer;

import com.teamresourceful.resourcefullib.client.components.CursorWidget;
import com.teamresourceful.resourcefullib.client.screens.CursorScreen;
import com.teamresourceful.resourcefullib.client.utils.ScreenUtils;
import dev.dediamondpro.minemark.elements.MineMarkElement;
import dev.dediamondpro.minemark.utils.MouseButton;
import earth.terrarium.hermes.styles.HermesStyle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.CommonComponents;

import java.io.Closeable;

public class HermesWidget extends AbstractWidget implements CursorWidget, Closeable {

    protected final MineMarkElement<HermesStyle, HermesRenderer> element;
    protected float scrollOffset = 0f;
    protected boolean errored = false;

    protected CursorScreen.Cursor cursor = CursorScreen.Cursor.DEFAULT;

    public HermesWidget(int x, int y, int width, int height, MineMarkElement<HermesStyle, HermesRenderer> element) {
        super(x, y, width, height, CommonComponents.EMPTY);
        this.element = element;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.scrollOffset = Math.max(-5, Math.min(this.scrollOffset, this.element.getHeight() - getHeight() + 5));
         try {
             graphics.enableScissor(getX(), getY(), getX() + getWidth(), getY() + getHeight());
             this.cursor = CursorScreen.Cursor.DEFAULT;
             HermesRenderer renderer = new HermesRenderer(
                     Minecraft.getInstance().font,
                     graphics,
                     cursor -> this.cursor = cursor
             );

             this.element.draw(
                     this.getX(),
                     this.getY() - this.scrollOffset,
                     this.getWidth(),
                     mouseX,
                     mouseY,
                     renderer
             );

             if (renderer.tooltip() != null) {
                 ScreenUtils.setTooltip(renderer.tooltip());
             }
         } catch (Exception e) {
             if (!this.errored) {
                 e.printStackTrace();
                 this.errored = true;
             }
         } finally {
             graphics.disableScissor();
         }
         if (this.errored) {
             graphics.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), 0x7F000000);
             graphics.drawString(
                     Minecraft.getInstance().font,
                     "An error occurred while rendering this element",
                     getX() + 5, getY() + 5,
                     -1
             );
         }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        MouseButton mouse = switch (button) {
            case 0 -> MouseButton.LEFT;
            case 1 -> MouseButton.RIGHT;
            case 2 -> MouseButton.MIDDLE;
            default -> null;
        };
        if (mouse == null || !this.isMouseOver(mouseX, mouseY)) return false;
        int x = getX();
        int y = getY() - (int) this.scrollOffset;
        this.element.onMouseClicked(x, y, mouse, (float) mouseX, (float) mouseY);
        return true;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        this.scrollOffset += (float) -scrollY * 15f;
        return true;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    @Override
    public CursorScreen.Cursor getCursor() {
        return this.cursor;
    }

    @Override
    public void close() {
        this.element.close();
    }
}
