package earth.terrarium.hermes.api.defaults.carousel;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teamresourceful.resourcefullib.client.scissor.ScissorBoxStack;
import com.teamresourceful.resourcefullib.client.utils.RenderUtils;
import earth.terrarium.hermes.api.TagElement;
import earth.terrarium.hermes.api.themes.Theme;
import earth.terrarium.hermes.utils.ElementParsingUtils;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CarouselTagElement implements TagElement {

    protected List<TagElement> children = new ArrayList<>();

    private final int height;

    private int index;

    public CarouselTagElement(Map<String, String> parameters) {
        this.index = ElementParsingUtils.parseInt(parameters, "index", 0);
        this.height = ElementParsingUtils.parseInt(parameters, "height", -1);
    }

    @Override
    public void render(Theme theme, PoseStack pose, ScissorBoxStack scissor, int x, int y, int width, int mouseX, int mouseY, boolean hovered, float partialTicks) {
        TagElement element = getCurrentChild();
        if (element != null) {
            if (this.height != -1) {
                try (var ignored = RenderUtils.createScissorBoxStack(scissor, Minecraft.getInstance(), pose, x + 20, y, width - 40, this.height)) {
                    element.render(theme, pose, scissor, x + 20, y, width - 40, mouseX, mouseY, hovered, partialTicks);
                }
            } else {
                element.render(theme, pose, scissor, x + 20, y, width - 40, mouseX, mouseY, hovered, partialTicks);
            }
        }
        int middle = (getHeight(width - 40) / 2);
        int relativeX = mouseX - x;
        int relativeY = mouseY - y;
        theme.drawCarouselButton(pose, x + 2, y + middle - 10, true, relativeX >= 2 && relativeX <= 16 && relativeY >= middle - 10 && relativeY <= middle + 10);
        theme.drawCarouselButton(pose, x + width - 16, y + middle - 10, false, relativeX >= width - 16 && relativeX <= width - 2 && relativeY >= middle - 10 && relativeY <= middle + 10);
    }

    @Override
    public int getHeight(int width) {
        if (this.height != -1) {
            return this.height;
        }
        TagElement element = getCurrentChild();
        if (element != null) {
            return Math.max(element.getHeight(width), 20);
        }
        return 20;
    }

    private TagElement getCurrentChild() {
        if (this.children.isEmpty() || this.index < 0 || this.index >= this.children.size()) {
            return null;
        }
        return this.children.get(this.index);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button, int width) {
        if (mouseY >= getHeight(width) || mouseY < 0) {
            return false;
        }
        int middle = (getHeight(width - 40) / 2);
        if (!this.children.isEmpty() && mouseY >= middle - 10 && mouseY <= middle + 10) {
            if (mouseX > 2 && mouseX <= 16) {
                this.index--;
                if (this.index < 0) {
                    this.index = this.children.size() - 1;
                }
                return true;
            }
            if (mouseX >= width - 16 && mouseX < width - 2) {
                this.index++;
                if (this.index >= this.children.size()) {
                    this.index = 0;
                }
                return true;
            }
        }
        mouseX -= 20;
        TagElement element = getCurrentChild();
        if (element != null) {
            return element.mouseClicked(mouseX, mouseY, button, width - 40);
        }
        return false;
    }

    @Override
    public void addChild(TagElement element) {
        if (!(element instanceof CarouselItemTagElement)) {
            throw new IllegalArgumentException("CarouselTagElement can only contain CarouselItemTagElements");
        }
        this.children.add(element);
    }

    @Override
    public @NotNull List<TagElement> getChildren() {
        return this.children;
    }
}
