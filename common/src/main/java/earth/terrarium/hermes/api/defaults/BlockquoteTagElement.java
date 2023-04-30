package earth.terrarium.hermes.api.defaults;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teamresourceful.resourcefullib.client.scissor.ScissorBoxStack;
import com.teamresourceful.resourcefullib.common.color.Color;
import com.teamresourceful.resourcefullib.common.color.ConstantColors;
import earth.terrarium.hermes.api.TagElement;
import earth.terrarium.hermes.utils.ElementParsingUtils;
import net.minecraft.client.gui.Gui;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BlockquoteTagElement implements TagElement {

    protected List<TagElement> children = new ArrayList<>();

    private final Color color;

    public BlockquoteTagElement(Map<String, String> parameters) {
        this.color = ElementParsingUtils.parseColor(parameters, "color", ConstantColors.white);
    }

    @Override
    public void render(PoseStack pose, ScissorBoxStack scissor, int x, int y, int width, int mouseX, int mouseY, float partialTicks) {
        Gui.fill(pose, x + 3, y, x + width, y + getHeight(width) - 2, -100, 0x80000000);
        Gui.fill(pose, x, y, x + 3, y + getHeight(width) - 2, -100, this.color.getValue() | 0xFF000000);
        int height = 2;
        for (TagElement element : this.children) {
            element.render(pose, scissor, x + 7, y + height, width - 7, mouseX, mouseY, partialTicks);
            height += element.getHeight(width);
        }
    }

    @Override
    public int getHeight(int width) {
        int height = 2;
        for (TagElement element : this.children) {
            height += element.getHeight(width);
        }
        return height + 4;
    }

    @Override
    public void addChild(TagElement element) {
        if (!(element instanceof TextTagElement)) {
            throw new IllegalArgumentException("Blockquote elements can only contain text elements.");
        }
        this.children.add(element);
    }

    @Override
    public @NotNull List<TagElement> getChildren() {
        return this.children;
    }
}
