package earth.terrarium.hermes.api.defaults.columns;

import earth.terrarium.hermes.api.Alignable;
import earth.terrarium.hermes.api.TagElement;
import earth.terrarium.hermes.api.themes.Theme;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ColumnsTagElement implements TagElement, Alignable {

    protected final List<TagElement> elements = new ArrayList<>();

    private final Function widthFunction;

    public ColumnsTagElement(Map<String, String> parameters) {
        if (parameters.containsKey("template")) {
            String template = parameters.get("template");
            String[] columns = template.split(" ");
            int[] widths = new int[columns.length];
            for (int i = 0; i < columns.length; i++) {
                widths[i] = columns[i].endsWith("%") ? Integer.parseInt(columns[i].substring(0, columns[i].length() - 1)) : Integer.parseInt(columns[i]);
            }
            this.widthFunction = (index, width) -> index < 0 || index >= widths.length ? 0 : (int) (width * widths[index] / 100f);
        } else {
            this.widthFunction = (index, width) -> (int) (width / (float) this.elements.size());
        }
    }

    @Override
    public void render(Theme theme, GuiGraphics graphics, int x, int y, int width, int mouseX, int mouseY, boolean hovered, float partialTicks) {
        int index = 0;
        int areaHeight = this.getHeight(width);
        for (TagElement element : elements) {
            int columnWidth = this.widthFunction.apply(index, width);
            int offsetY = 0;
            if (element instanceof ColumnTagElement column) {
                offsetY = getOffset(areaHeight, element.getHeight(columnWidth), column.vAlign);
            }
            element.render(theme, graphics, x, y + offsetY, columnWidth, mouseX, mouseY, hovered, partialTicks);
            index++;
            x += columnWidth;
        }
    }

    @Override
    public int getHeight(int width) {
        int height = 0;
        int index = 0;
        for (TagElement element : elements) {
            int columnWidth = this.widthFunction.apply(index, width);
            height = Math.max(height, element.getHeight(columnWidth));
            index++;
        }
        return height;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button, int width) {
        int columnWidth = width / this.elements.size();
        int index = 0;
        for (TagElement element : elements) {
            if (element.mouseClicked(mouseX - (columnWidth * index), mouseY, button, columnWidth)) {
                return true;
            }
            index++;
        }
        return false;
    }

    @Override
    public void addChild(TagElement element) {
        if (!(element instanceof ColumnTagElement)) {
            throw new IllegalArgumentException("Columns can only contain columns.");
        }
        if (this.elements.size() > 2) {
            throw new IllegalArgumentException("Columns can only contain up to three columns.");
        }
        this.elements.add(element);
    }

    @Override
    public @NotNull List<TagElement> getChildren() {
        return this.elements;
    }

    interface Function {
        int apply(int index, int width);
    }
}
