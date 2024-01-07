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

import java.util.*;

public class ComponentTagElement extends TextTagElement {

    protected Component text = CommonComponents.EMPTY;

    public ComponentTagElement(Map<String, String> parameters) {
        super(parameters);
    }

    @Override
    public void render(Theme theme, GuiGraphics graphics, int x, int y, int width, int mouseX, int mouseY, boolean hovered, float partialTicks) {
        Component renderText = text.copy().setStyle(this.getStyle().applyTo(text.getStyle()));
        var font = Minecraft.getInstance().font;
        var lines = font.split(renderText, width - (10 + (2 * xMargin)));

        var contentWidth = lines.stream().mapToInt((line) -> font.width(line) - 1).max().orElse(0);
        // from top of top row capitals, to bottom of bottom row letters with descenders (eg: "y")
        int contentHeight = (lines.size() * font.lineHeight) + (lines.size() - 2);
        int[] lineOffsets = lines.stream().mapToInt((line) -> getOffsetForTextTag(width, line)).toArray();
        int contentOffset = Arrays.stream(lineOffsets).min().orElse(width);

        drawBackground(graphics, x + xMargin + contentOffset, y + yMargin, contentWidth, contentHeight);

        int lineHeight = font.lineHeight;
        for (int i = 0; i < lines.size(); i++) {
            graphics.drawString(
                    font,
                    lines.get(i),
                    x + xMargin + lineOffsets[i],
                    y + yMargin + (i * (lineHeight + 1)),
                    this.color.getValue(),
                    this.shadowed
            );

            if (mouseY >= (y + yMargin + contentHeight) || mouseY < (y + yMargin) || !hovered) {
                continue;
            }
            renderComponentHoverEffect(font.getSplitter().componentStyleAtWidth(lines.get(i), Mth.floor(x - mouseX)));
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
        int lines = Minecraft.getInstance().font.split(this.text, width - (10 + (2 * xMargin))).size();
        int lineHeight = Minecraft.getInstance().font.lineHeight;
        return ((lines * lineHeight) + (lines - 2)) + (2 * yMargin); // element height + vertical spacing
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
