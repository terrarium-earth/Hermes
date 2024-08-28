package earth.terrarium.hermes;

import com.mojang.blaze3d.platform.InputConstants;
import com.teamresourceful.resourcefullib.client.components.CursorWidget;
import com.teamresourceful.resourcefullib.client.screens.CursorScreen;
import com.teamresourceful.resourcefullib.client.utils.ScreenUtils;
import dev.dediamondpro.minemark.elements.MineMarkElement;
import dev.dediamondpro.minemark.utils.MouseButton;
import earth.terrarium.hermes.api.links.LinkHandler;
import earth.terrarium.hermes.api.rendering.HtmlRenderer;
import earth.terrarium.hermes.api.rendering.HtmlStyle;
import earth.terrarium.hermes.impl.HermesRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.CommonComponents;
import org.joml.Vector2f;

import java.io.Closeable;
import java.util.function.Supplier;

public class HermesWidget extends AbstractWidget implements CursorWidget, Closeable {

    private static final int NO_SCROLL = 10;
    private static final int SCROLL_DIVISOR = 20;
    private static final int MAX_SCROLL = 100;

    protected final MineMarkElement<HtmlStyle, HtmlRenderer> element;
    protected float scrollOffset = 0f;
    protected boolean errored = false;

    protected CursorScreen.Cursor cursor = CursorScreen.Cursor.DEFAULT;

    protected Vector2f autoScrollPosition = null;

    public HermesWidget(int x, int y, int width, int height, MineMarkElement<HtmlStyle, HtmlRenderer> element) {
        super(x, y, width, height, CommonComponents.EMPTY);
        this.element = element;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        if (this.autoScrollPosition != null) {
            float scroll = this.autoScrollPosition.y - mouseY;
            if (scroll > NO_SCROLL) {
                this.scrollOffset -= Math.min(scroll / SCROLL_DIVISOR, MAX_SCROLL);
            } else if (scroll < -NO_SCROLL) {
                this.scrollOffset += Math.min(-scroll / SCROLL_DIVISOR, MAX_SCROLL);
            }
        }

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

            if (this.cursor == CursorScreen.Cursor.DEFAULT && this.autoScrollPosition != null) {
                this.cursor = CursorScreen.Cursor.RESIZE_NS;
            }

            if (renderer.getTooltip() != null) {
                ScreenUtils.setTooltip(renderer.getTooltip());
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
        if (mouse == MouseButton.MIDDLE && this.autoScrollPosition == null) {
            this.autoScrollPosition = new Vector2f((float) mouseX, (float) mouseY);
        } else {
            this.autoScrollPosition = null;
        }
        return true;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (this.autoScrollPosition != null) return true;
        this.scrollOffset += (float) -scrollY * 20f;
        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        this.autoScrollPosition = null;
        return switch (keyCode) {
            case InputConstants.KEY_PAGEUP -> {
                this.scrollOffset -= 100;
                yield true;
            }
            case InputConstants.KEY_PAGEDOWN -> {
                this.scrollOffset += 100;
                yield true;
            }
            case InputConstants.KEY_HOME -> {
                this.scrollOffset = -5;
                yield true;
            }
            case InputConstants.KEY_END -> {
                this.scrollOffset = this.element.getHeight() - getHeight() + 5;
                yield true;
            }
            case InputConstants.KEY_UP -> {
                this.scrollOffset -= 10;
                yield true;
            }
            case InputConstants.KEY_DOWN -> {
                this.scrollOffset += 10;
                yield true;
            }
            default -> super.keyPressed(keyCode, scanCode, modifiers);
        };
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

    public record ProtocolHandler(Supplier<HermesWidget> widget) implements LinkHandler {

        @Override
        public int priority() {
            return 0;
        }

        @Override
        public boolean canHandle(String url) {
            return url.equals("#");
        }

        @Override
        public void handle(String url) {
            widget.get().scrollOffset = 0;
            widget.get().autoScrollPosition = null;
        }
    }
}
