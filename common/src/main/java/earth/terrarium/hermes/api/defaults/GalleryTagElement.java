package earth.terrarium.hermes.api.defaults;

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

public class GalleryTagElement implements TagElement {

    protected List<TagElement> children = new ArrayList<>();

    private final int height;

    private int index = 0;

    public GalleryTagElement(Map<String, String> parameters) {
        this.height = ElementParsingUtils.parseInt(parameters, "height", -1);
    }

    @Override
    public void render(Theme theme, PoseStack pose, ScissorBoxStack scissor, int x, int y, int width, int mouseX, int mouseY, float partialTicks) {
        TagElement element = getCurrentChild();
        if (element != null) {
            if (this.height == -1) {
                element.render(theme, pose, scissor, x, y, width, mouseX, mouseY, partialTicks);
            }
            try (var ignored = RenderUtils.createScissorBoxStack(scissor, Minecraft.getInstance(), pose, x, y, width, this.height)) {
                element.render(theme, pose, scissor, x, y, width, mouseX, mouseY, partialTicks);
            }
        }
    }

    @Override
    public int getHeight(int width) {
        if (this.height == -1) {
            TagElement element = getCurrentChild();
            if (element != null) {
                return element.getHeight(width);
            }
            return 0;
        }
        return this.height;
    }

    private TagElement getCurrentChild() {
        if (this.children.isEmpty() || this.index < 0 || this.index >= this.children.size()) {
            return null;
        }
        return this.children.get(this.index);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button, int width) {
        if (mouseY >= this.getHeight(width) || mouseY < 0) {
            return false;
        }
        if (this.children.isEmpty()) {
            return false;
        }
        if (mouseX <= width / 4d) {
            this.index--;
            if (this.index < 0) {
                this.index = this.children.size() - 1;
            }
            return true;
        }
        if (mouseX >= width - width / 4d) {
            this.index++;
            if (this.index >= this.children.size()) {
                this.index = 0;
            }
            return true;
        }
        return false;
    }

    @Override
    public void addChild(TagElement element) {
        if (!(element instanceof ImageTagElement)) {
            throw new IllegalArgumentException("Gallery tag elements can only contain image tag elements.");
        }
        this.children.add(element);
    }

    @Override
    public @NotNull List<TagElement> getChildren() {
        return this.children;
    }
}
