package earth.terrarium.hermes.client;

import com.teamresourceful.resourcefullib.client.utils.RenderUtils;
import earth.terrarium.hermes.api.TagElement;
import earth.terrarium.hermes.api.themes.Theme;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DocumentWidget extends ContainerWidget {

    private final List<TagElement> elements = new ArrayList<>();
    private final Theme theme;

    private final double overscrollTop;
    private final double overscrollBottom;

    // scrollBarUIWidth includes scrollbarWidth and space separating it from content
    private static final int SCROLL_BAR_UI_WIDTH = 5;
    private static final int SCROLL_BAR_WIDTH = 3;
    private boolean scrolling = false;
    private double scrollAmount;
    private int lastFullHeight;

    //We have to defer the mouse click until during render because we don't know the height of the document until then.
    private DocumentMouse mouse = null;

    public DocumentWidget(int x, int y, int width, int height, double overscrollTop, double overscrollBottom, Theme theme, List<TagElement> elements) {
        super(x, y, width, height);
        this.overscrollTop = overscrollTop;
        this.overscrollBottom = overscrollBottom;
        this.lastFullHeight = this.height;
        this.theme = theme;
        this.elements.addAll(elements);
        this.scrollAmount = -overscrollTop;
    }

    public DocumentWidget(int x, int y, int width, int height, Theme theme, List<TagElement> elements) {
        this(x, y, width, height, 0.0D, 0.0D, theme, elements);
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        int x = this.getX();
        int y = this.getY();

        int fullHeight = 0;
        // Center content area leaving room for scrollbarUIWidth on both sides, though there is only one
        int contentWidth = this.width - (2 * SCROLL_BAR_UI_WIDTH);
        int contentX = this.getX() + SCROLL_BAR_UI_WIDTH;
        try (var ignored = RenderUtils.createScissor(Minecraft.getInstance(), graphics, x - 5, y, width + 10, height)) {
            for (TagElement element : this.elements) {
                if (this.mouse != null && element.mouseClicked(this.mouse.x() - contentX, this.mouse.y() - (y - this.scrollAmount), this.mouse.button(), contentWidth)) {
                    this.mouse = null;
                }
                element.render(this.theme, graphics, contentX, y - (int) this.scrollAmount, contentWidth, mouseX, mouseY, this.isMouseOver(mouseX, mouseY), partialTicks);
                var itemHeight = element.getHeight(contentWidth);
                y += itemHeight;
                fullHeight += itemHeight;
            }
            this.mouse = null;
            this.lastFullHeight = fullHeight;
        }

        if (this.lastFullHeight > this.height) {
            int scrollBarHeight = (int) ((this.height / (double) this.lastFullHeight) * this.height);
            int scrollBarX = this.getX() + this.width - SCROLL_BAR_WIDTH;
            int scrollBarY = this.getY() + 4 + (int) ((this.scrollAmount / (double) this.lastFullHeight) * this.height);
            int scrollBarColor = this.isMouseOver(mouseX, mouseY) && mouseX >= scrollBarX && mouseX <= scrollBarX + SCROLL_BAR_WIDTH && mouseY >= scrollBarY && mouseY <= scrollBarY + scrollBarHeight ? 0xFFF0F0F0 : 0xFFC0C0C0;
            graphics.fill(scrollBarX, scrollBarY, scrollBarX + SCROLL_BAR_WIDTH, scrollBarY + scrollBarHeight, scrollBarColor);
        }
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (this.scrolling) {
            double scrollBarHeight = (this.height / (double) this.lastFullHeight) * this.height;
            double scrollBarDragY = dragY / (this.height - scrollBarHeight);
            this.scrollAmount = Mth.clamp(
                    this.scrollAmount + scrollBarDragY * this.lastFullHeight, -overscrollTop,
                    Math.max(-overscrollTop, this.lastFullHeight - this.height + overscrollBottom)
            );
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollAmount) {
        this.scrollAmount = Mth.clamp(this.scrollAmount - scrollAmount * 10, -overscrollTop, Math.max(-overscrollTop, this.lastFullHeight - this.height + overscrollBottom));
        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isMouseOver(mouseX, mouseY)) {
            if (isMouseOverScrollBar(mouseX, mouseY)) {
                this.scrolling = true;
                return true;
            } else {
                this.mouse = new DocumentMouse(mouseX, mouseY, button);
            }
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double d, double e, int i) {
        if (i == 0) {
            this.scrolling = false;
        }

        return super.mouseReleased(d, e, i);
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return mouseX >= this.getX() && mouseX <= this.getX() + this.width && mouseY >= this.getY() && mouseY <= this.getY() + this.height;
    }

    @Override
    public @NotNull List<? extends GuiEventListener> children() {
        return List.of();
    }

    private boolean isMouseOverScrollBar(double mouseX, double mouseY) {
        return this.lastFullHeight > this.height &&
                mouseX >= this.getX() + this.width - SCROLL_BAR_WIDTH && mouseX <= this.getX() + this.width &&
                mouseY >= this.getY() + 4 && mouseY <= this.getY() + this.height - 4;
    }
}
