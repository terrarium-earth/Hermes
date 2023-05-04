package earth.terrarium.hermes.api.defaults.lists;

import com.teamresourceful.resourcefullib.common.color.Color;
import com.teamresourceful.resourcefullib.common.color.ConstantColors;
import earth.terrarium.hermes.api.TagElement;
import earth.terrarium.hermes.utils.ElementParsingUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class ListTagElement implements TagElement {

    protected List<TagElement> children = new ArrayList<>();

    protected final Color color;

    public ListTagElement(Map<String, String> parameters) {
        this.color = ElementParsingUtils.parseColor(parameters, "color", ConstantColors.white);
    }

    abstract int getItemHeight(int index, TagElement element, int width);

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button, int width) {
        int index = 0;
        for (TagElement element : this.children) {
            if (element.mouseClicked(mouseX, mouseY, button, width)) {
                return true;
            }
            mouseY -= this.getItemHeight(index, element, width);
        }
        return false;
    }

    @Override
    public int getHeight(int width) {
        int i = 0;
        int height = 0;
        for (TagElement element : this.children) {
            height += this.getItemHeight(i, element, width);
            i++;
        }
        return height;
    }

    @Override
    public void addChild(TagElement element) {
        if (!(element instanceof ListItemTagElement)) {
            throw new IllegalArgumentException("Lists can only contain list items.");
        }
        this.children.add(element);
    }

    @Override
    public @NotNull List<TagElement> getChildren() {
        return this.children;
    }
}
