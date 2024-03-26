package earth.terrarium.hermes.api.defaults;

import com.teamresourceful.resourcefullib.common.color.Color;
import com.teamresourceful.resourcefullib.common.color.ConstantColors;
import earth.terrarium.hermes.api.TagElement;
import earth.terrarium.hermes.api.themes.Theme;
import earth.terrarium.hermes.utils.ElementParsingUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HintTagElement implements TagElement {

    protected List<TagElement> children = new ArrayList<>();

    private final Color color;
    private final Item icon;
    private final String title;

    public HintTagElement(Map<String, String> parameters) {
        this.color = ElementParsingUtils.parseColor(parameters, "color", ConstantColors.white);
        this.icon = ElementParsingUtils.parseItem(parameters, "icon", Items.AIR);
        this.title = parameters.getOrDefault("title", "");
    }

    @Override
    public void render(Theme theme, GuiGraphics graphics, int x, int y, int width, int mouseX, int mouseY, boolean hovered, float partialTicks) {
        graphics.fill(x, y, x + width, y + 20, this.color.getValue() | 0x80000000);
        graphics.fill(x, y + 20, x + width, y + getHeight(width), 0x80000000);
        graphics.renderOutline(x, y, width, getHeight(width), this.color.getValue() | 0xFF000000);
        theme.drawText(
            graphics, this.title,
            x + 22, y + 6,
            ConstantColors.white, false
        );
        int height = 23;
        for (TagElement element : this.children) {
            element.render(theme, graphics, x + 7, y + height, width - 7, mouseX, mouseY, hovered, partialTicks);
            height += element.getHeight(width);
        }
        graphics.renderItem(this.icon.getDefaultInstance(), x + 2, y + 2);
    }

    @Override
    public int getHeight(int width) {
        int height = 20;
        for (TagElement element : this.children) {
            height += element.getHeight(width);
        }
        return height + 4;
    }

    @Override
    public void addChild(TagElement element) {
        if (!(element instanceof TextTagElement)) {
            throw new IllegalArgumentException("Hint elements can only contain text elements.");
        }
        this.children.add(element);
    }

    @Override
    public @NotNull List<TagElement> getChildren() {
        return this.children;
    }
}
