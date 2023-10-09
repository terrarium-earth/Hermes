package earth.terrarium.hermes.api.defaults;

import earth.terrarium.hermes.api.themes.Theme;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ComponentTagElement extends TextTagElement {

    protected Component text = CommonComponents.EMPTY;

    public ComponentTagElement(Map<String, String> parameters) {
        super(parameters);
    }

    @Override
    public void render(Theme theme, GuiGraphics graphics, int x, int y, int width, int mouseX, int mouseY, boolean hovered, float partialTicks) {
        int height = 0;
        Component renderText = text.copy().setStyle(this.getStyle().applyTo(text.getStyle()));
        for (FormattedCharSequence sequence : Minecraft.getInstance().font.split(renderText, width - 10)) {
            graphics.drawString(
                Minecraft.getInstance().font,
                sequence, getXOffset(x, width, sequence), y + height, this.color.getValue(),
                this.shadowed
            );
            height += Minecraft.getInstance().font.lineHeight + 1;
            if (mouseY >= y + height || mouseY < y || !hovered) {
                continue;
            }
            renderComponentHoverEffect(Minecraft.getInstance().font.getSplitter().componentStyleAtWidth(sequence, Mth.floor(x - mouseX)));
        }
    }

    @Override
    public void setContent(String content) {
        try {
            this.text = Component.Serializer.fromJson(content);
        } catch (Exception e) {
            this.text = CommonComponents.EMPTY;
        }
    }

    @Override
    public int getHeight(int width) {
        int lines = Minecraft.getInstance().font.split(this.text, width - 10).size();
        return lines * (Minecraft.getInstance().font.lineHeight + 1);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button, int width) {
        if (mouseY >= this.getHeight(width) || mouseY < 0) {
            return false;
        }
        Style style = Minecraft.getInstance().font.getSplitter().componentStyleAtWidth(this.text, Mth.floor(mouseX));
        return Minecraft.getInstance().screen != null && Minecraft.getInstance().screen.handleComponentClicked(style);
    }

    protected void renderComponentHoverEffect(@Nullable Style style) {
        Screen screen = Minecraft.getInstance().screen;
        if (screen == null) return;
        if (style != null && style.getHoverEvent() != null) {
            List<FormattedCharSequence> tooltip = new ArrayList<>();
            HoverEvent event = style.getHoverEvent();
            var itemInfo = event.getValue(HoverEvent.Action.SHOW_ITEM);
            if (itemInfo != null) {
                for (Component component : Screen.getTooltipFromItem(Minecraft.getInstance(), itemInfo.getItemStack())) {
                    tooltip.add(component.getVisualOrderText());
                }
            } else {
                var entityInfo = event.getValue(HoverEvent.Action.SHOW_ENTITY);
                if (entityInfo != null) {
                    if (Minecraft.getInstance().options.advancedItemTooltips) {
                        entityInfo.getTooltipLines().forEach((component) -> tooltip.add(component.getVisualOrderText()));
                    }
                } else {
                    Component component = event.getValue(HoverEvent.Action.SHOW_TEXT);
                    if (component != null) {
                        tooltip.addAll(Minecraft.getInstance().font.split(component, Math.max(screen.width / 2, 200)));
                    }
                }
            }

            if (!tooltip.isEmpty()) {
                screen.setTooltipForNextRenderPass(tooltip);
            }
        }
    }
}
