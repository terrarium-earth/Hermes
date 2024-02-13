package earth.terrarium.hermes.api.defaults;

import com.teamresourceful.resourcefullib.common.color.Color;
import com.teamresourceful.resourcefullib.common.color.ConstantColors;
import earth.terrarium.hermes.api.TagElement;
import earth.terrarium.hermes.api.themes.Theme;
import earth.terrarium.hermes.utils.ElementParsingUtils;
import net.minecraft.client.gui.GuiGraphics;
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
    public void render(Theme theme, GuiGraphics graphics, int x, int y, int width, int mouseX, int mouseY, boolean hovered, float partialTicks) {
        graphics.fill(x + 3, y, x + width, y + getHeight(width) - 2, 0x80000000);
        graphics.fill(x, y, x + 3, y + getHeight(width) - 2, this.color.getValue() | 0xFF000000);
        int height = 2;
        for (TagElement element : this.children) {
            element.render(theme, graphics, x + 7, y + height, width - 7, mouseX, mouseY, hovered, partialTicks);
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
        if (!(element instanceof TextTagElement) && !(element instanceof TextContentTagElement)) {
            throw new IllegalArgumentException("Blockquote elements can only contain text elements.");
        }
        this.children.add(element);
    }

    @Override
    public @NotNull List<TagElement> getChildren() {
        return this.children;
    }
}
