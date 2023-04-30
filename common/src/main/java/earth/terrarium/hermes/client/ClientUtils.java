package earth.terrarium.hermes.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.ArrayList;
import java.util.List;

public final class ClientUtils {

    public static void renderTooltip(ItemStack stack) {
        if (Minecraft.getInstance().player == null) return;
        renderTooltip(stack.getTooltipLines(
            Minecraft.getInstance().player,
            Minecraft.getInstance().options.advancedItemTooltips ? TooltipFlag.ADVANCED : TooltipFlag.NORMAL
        ));
    }

    public static void renderTooltip(Component component) {
        if (Minecraft.getInstance().screen != null) {
            Minecraft.getInstance().screen.setTooltipForNextRenderPass(List.of(component.getVisualOrderText()));
        }
    }

    public static void renderTooltip(List<Component> components) {
        if (Minecraft.getInstance().screen != null) {
            List<FormattedCharSequence> formatted = new ArrayList<>(components.size());
            for (Component component : components) {
                formatted.add(component.getVisualOrderText());
            }
            Minecraft.getInstance().screen.setTooltipForNextRenderPass(formatted);
        }
    }

    public static void renderItemWithCount(PoseStack pose, ItemStack stack, int x, int y) {
        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
        renderer.renderAndDecorateItem(pose, stack, x, y);
        renderer.renderGuiItemDecorations(pose, Minecraft.getInstance().font, stack, x, y);
    }
}
