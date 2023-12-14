package earth.terrarium.hermes.api.defaults;

import com.teamresourceful.resourcefullib.client.CloseablePoseStack;
import earth.terrarium.hermes.api.Alignable;
import earth.terrarium.hermes.api.TagElement;
import earth.terrarium.hermes.api.themes.Theme;
import earth.terrarium.hermes.utils.ElementParsingUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Map;

public class ItemTagElement implements TagElement, Alignable {

    protected final ItemStack output;
    protected final float scale;
    protected final Alignment align;

    public ItemTagElement(Map<String, String> parameters) {
        Item item = ElementParsingUtils.parseItem(parameters, "id", Items.AIR);
        CompoundTag tag = ElementParsingUtils.parseTag(parameters, "tag", null);
        ItemStack stack = new ItemStack(item);
        if (tag != null) stack.setTag(tag);
        this.output = stack;
        this.scale = ElementParsingUtils.parseFloat(parameters, "scale", 1.0F);
        this.align = ElementParsingUtils.parseAlignment(parameters, "align", Alignment.CENTER);
    }

    @Override
    public void render(Theme theme, GuiGraphics graphics, int x, int y, int width, int mouseX, int mouseY, boolean hovered, float partialTicks) {
        try (var pose = new CloseablePoseStack(graphics)) {

            int actualX = switch (align) {
                case LEFT -> x;
                case CENTER -> x + (int) (((width / 2f) - (8 * scale)) + 0.5f);
                case RIGHT -> x + (int) (width - (16 * scale) + 0.5f);
            };

            pose.translate(actualX, y + 1, 0);
            pose.scale(scale, scale, 1.0F);
            graphics.renderFakeItem(output, 0, 0);
        }
    }

    @Override
    public int getHeight(int width) {
        return Mth.ceil(18 * scale);
    }
}
