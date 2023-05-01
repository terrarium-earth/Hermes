package earth.terrarium.hermes.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teamresourceful.resourcefullib.client.scissor.ScissorBoxStack;
import com.teamresourceful.resourcefullib.client.utils.RenderUtils;
import earth.terrarium.hermes.api.TagElement;
import earth.terrarium.hermes.api.themes.Theme;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.AbstractContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DocumentWidget extends AbstractContainerEventHandler implements Renderable, NarratableEntry {

    private final List<TagElement> elements = new ArrayList<>();
    private final Theme theme;

    private final int x;
    private final int y;
    private final int width;
    private final int height;

    private double scrollAmount;
    private int lastFullHeight;

    private DocumentMouse mouse = null; //We have to defer the mouse click until during render because we don't know the height of the document until then.

    public DocumentWidget(int x, int y, int width, int height, Theme theme, List<TagElement> elements) {
        this.x = x;
        this.y = y;
        this.width = width - 6;
        this.height = height - 6;
        this.lastFullHeight = this.height;
        this.theme = theme;
        this.elements.addAll(elements);
    }

    @Override
    public void render(@NotNull PoseStack pose, int mouseX, int mouseY, float partialTicks) {
        int x = this.x;
        int y = this.y;

        int fullHeight = 0;
        try (var scissor = RenderUtils.createScissorBoxStack(new ScissorBoxStack(), Minecraft.getInstance(), pose, x, y, width, height)) {
            for (TagElement element : this.elements) {
                if (this.mouse != null && element.mouseClicked(this.mouse.x() - x, this.mouse.y() - (y - this.scrollAmount), this.mouse.button(), this.width)) {
                    this.mouse = null;
                }
                element.render(this.theme, pose, scissor.stack(), x, y - (int) this.scrollAmount, this.width, mouseX, mouseY, this.isMouseOver(mouseX, mouseY), partialTicks);
                var itemheight = element.getHeight(this.width);
                y += itemheight;
                fullHeight += itemheight;
            }
            this.mouse = null;
            this.lastFullHeight = fullHeight;
        }
        this.scrollAmount = Mth.clamp(this.scrollAmount, 0.0D, Math.max(0, this.lastFullHeight - this.height));
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollAmount) {
        this.scrollAmount = Mth.clamp(this.scrollAmount - scrollAmount * 10, 0.0D, Math.max(0, this.lastFullHeight - this.height));
        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isMouseOver(mouseX, mouseY)) {
            this.mouse = new DocumentMouse(mouseX, mouseY, button);
        }
        return false;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y && mouseY <= this.y + this.height;
    }

    @Override
    public @NotNull List<? extends GuiEventListener> children() {
        return List.of();
    }

    @Override
    public @NotNull NarrationPriority narrationPriority() {
        return NarrationPriority.NONE;
    }

    @Override
    public void updateNarration(@NotNull NarrationElementOutput output) {

    }
}
