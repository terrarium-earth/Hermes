package earth.terrarium.hermes.api.defaults.columns;

import earth.terrarium.hermes.api.Alignment;
import earth.terrarium.hermes.api.TagElement;
import earth.terrarium.hermes.api.themes.Theme;
import earth.terrarium.hermes.utils.ElementParsingUtils;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ColumnTagElement implements TagElement {

    protected List<TagElement> children = new ArrayList<>();
    protected Alignment vAlign;

    public ColumnTagElement(Map<String, String> parameters) {
        this.vAlign = ElementParsingUtils.parseAlignment(parameters, "valign", Alignment.MIDDLE);
    }

    @Override
    public void render(Theme theme, GuiGraphics graphics, int x, int y, int width, int mouseX, int mouseY, boolean hovered, float partialTicks) {
        int height = 0;
        for (TagElement element : this.children) {
            element.render(theme, graphics, x, y + height, width, mouseX, mouseY, hovered, partialTicks);
            height += element.getHeight(width);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button, int width) {
        int height = 0;
        for (TagElement element : this.children) {
            if (element.mouseClicked(mouseX, mouseY - height, button, width)) {
                return true;
            }
            height += element.getHeight(width);
        }
        return false;
    }

    @Override
    public int getHeight(int width) {
        int height = 0;
        for (TagElement element : this.children) {
            height += element.getHeight(width);
        }
        return height;
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
