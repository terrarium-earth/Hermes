package earth.terrarium.hermes.api.defaults;

import com.teamresourceful.resourcefullib.common.color.Color;
import com.teamresourceful.resourcefullib.common.color.ConstantColors;
import earth.terrarium.hermes.api.TagElement;
import earth.terrarium.hermes.api.themes.Theme;
import earth.terrarium.hermes.utils.ElementParsingUtils;
import net.minecraft.client.gui.GuiGraphics;

import java.util.Map;

public class HorizontalRuleTagElement implements TagElement {

    protected final Color color;

    public HorizontalRuleTagElement(Map<String, String> parameters) {
        this.color = ElementParsingUtils.parseColor(parameters, "color", ConstantColors.gray);
    }

    @Override
    public void render(Theme theme, GuiGraphics graphics, int x, int y, int width, int mouseX, int mouseY, boolean hovered, float partialTicks) {
        graphics.fill(x, y + 4, x + width, y + 5, this.color.getValue() | 0xFF000000);
    }

    @Override
    public int getHeight(int width) {
        return 10;
    }
}
