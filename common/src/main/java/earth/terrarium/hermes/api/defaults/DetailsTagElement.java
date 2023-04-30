package earth.terrarium.hermes.api.defaults;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teamresourceful.resourcefullib.client.scissor.ScissorBoxStack;
import com.teamresourceful.resourcefullib.common.color.ConstantColors;
import earth.terrarium.hermes.api.TagElement;
import earth.terrarium.hermes.utils.ElementParsingUtils;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DetailsTagElement implements TagElement {

    protected List<TagElement> children = new ArrayList<>();

    private final String summary;

    private boolean open;

    public DetailsTagElement(Map<String, String> parameters) {
        this.summary = parameters.getOrDefault("summary", "");
        this.open = ElementParsingUtils.parseBoolean(parameters, "open", false);
    }

    @Override
    public void render(PoseStack pose, ScissorBoxStack scissor, int x, int y, int width, int mouseX, int mouseY, float partialTicks) {
        String text = (open ? "▼" : "▶") + (this.summary.isEmpty() ? "Details" : this.summary);
        Minecraft.getInstance().font.draw(pose, text, x, y + 2, ConstantColors.white.getValue());
        if (open) {
            int height = 4 + Minecraft.getInstance().font.lineHeight;
            for (TagElement element : this.children) {
                element.render(pose, scissor, x + 7, y + height, width - 7, mouseX, mouseY, partialTicks);
                height += element.getHeight(width);
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button, int width) {
        String text = (open ? "▼" : "▶") + (this.summary.isEmpty() ? "Details" : this.summary);
        if (mouseX < 0 || mouseX > Minecraft.getInstance().font.width(text) || mouseY < 0 || mouseY > 2 + Minecraft.getInstance().font.lineHeight) {
            mouseY -= 2 + Minecraft.getInstance().font.lineHeight;
            for (TagElement child : this.children) {
                if (child.mouseClicked(mouseX, mouseY, button, width)) {
                    return true;
                }
                mouseY -= child.getHeight(width);
            }
            return false;
        }
        this.open = !this.open;
        return true;
    }

    @Override
    public int getHeight(int width) {
        int height = 4 + Minecraft.getInstance().font.lineHeight;
        if (!open) return height;
        for (TagElement element : this.children) {
            height += element.getHeight(width);
        }
        return height + 2;
    }

    @Override
    public void addChild(TagElement element) {
        this.children.add(element);
    }

    @Override
    public @NotNull List<TagElement> getChildren() {
        return this.children;
    }
}
