package earth.terrarium.hermes.api.themes;

import com.teamresourceful.resourcefullib.common.color.ConstantColors;
import earth.terrarium.hermes.Hermes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class DefaultTheme implements Theme {

    private static final ResourceLocation DETAILS = new ResourceLocation(Hermes.MOD_ID, "textures/gui/details.png");
    private static final ResourceLocation CRAFTING = new ResourceLocation(Hermes.MOD_ID, "textures/gui/crafting.png");
    private static final ResourceLocation CAROUSEL = new ResourceLocation(Hermes.MOD_ID, "textures/gui/carousel.png");

    @Override
    public void drawDropdown(GuiGraphics graphics, int x, int y, int width, boolean hovered, boolean open, String text) {
        int vOffset = hovered ? 20 : 0;
        graphics.blit(DETAILS, x, y, 0, vOffset, 2, 20);
        graphics.blitRepeating(DETAILS, x + 2, y, width - 4, 20, 2, vOffset, 196, 20);
        graphics.blit(DETAILS, x + width - 2, y, 198, vOffset, 2, 20);
        if (open) {
            graphics.blit(DETAILS, x + 4, y + 6, 0, 40, 11, 7);
        } else {
            graphics.blit(DETAILS, x + 7, y + 4, 11, 40, 7, 11);
        }
        graphics.drawString(Minecraft.getInstance().font, text, x + 20, y + 6, ConstantColors.white.getValue(), false);
    }

    @Override
    public void drawSlot(GuiGraphics graphics, int x, int y, boolean hovered) {
        graphics.blit(CRAFTING, x, y, 24, 0, 18, 18);
        if (hovered) {
            graphics.fill(x + 1, y + 1, x + 17, y + 17, 0x80FFFFFF);
        }
    }

    @Override
    public void drawArrow(GuiGraphics graphics, int x, int y) {
        graphics.blit(CRAFTING, x, y, 42, 0, 22, 16);
    }

    @Override
    public void drawCraftingBackground(GuiGraphics graphics, int x, int y, int width, int height) {
        graphics.blitNineSliced(CRAFTING, x, y, width, height, 4, 4, 4, 4, 24, 24, 0, 0);
    }

    @Override
    public void drawCarouselButton(GuiGraphics graphics, int x, int y, boolean left, boolean hovered) {
        graphics.blit(CAROUSEL, x, y, left ? 0 : 14, hovered ? 20 : 0, 14, 20);
    }
}
