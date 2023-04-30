package earth.terrarium.hermes.api.themes;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teamresourceful.resourcefullib.client.utils.RenderUtils;
import com.teamresourceful.resourcefullib.common.color.ConstantColors;
import earth.terrarium.hermes.Hermes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.resources.ResourceLocation;

public class DefaultTheme implements Theme {

    private static final ResourceLocation DETAILS = new ResourceLocation(Hermes.MOD_ID, "textures/gui/details.png");
    private static final ResourceLocation CRAFTING = new ResourceLocation(Hermes.MOD_ID, "textures/gui/crafting.png");

    @Override
    public void drawDropdown(PoseStack pose, int x, int y, int width, boolean hovered, boolean open, String text) {
        RenderUtils.bindTexture(DETAILS);
        int vOffset = hovered ? 20 : 0;
        Gui.blit(pose, x, y, 0, vOffset, 2, 20);
        Gui.blitRepeating(pose, x + 2, y, width - 4, 20, 2, vOffset, 196, 20);
        Gui.blit(pose, x + width - 2, y, 198, vOffset, 2, 20);
        if (open) {
            Gui.blit(pose, x + 4, y + 6, 0, 40, 11, 7);
        } else {
            Gui.blit(pose, x + 4, y + 6, 11, 40, 11, 7);
        }
        Minecraft.getInstance().font.draw(pose, text, x + 20, y + 6, ConstantColors.white.getValue());
    }

    @Override
    public void drawSlot(PoseStack pose, int x, int y, boolean hovered) {
        RenderUtils.bindTexture(CRAFTING);
        Gui.blit(pose, x, y, 24, 0, 18, 18);
        if (hovered) {
            Gui.fill(pose, x + 1, y + 1, x + 17, y + 17, 0x80FFFFFF);
        }
    }

    @Override
    public void drawArrow(PoseStack pose, int x, int y) {
        RenderUtils.bindTexture(CRAFTING);
        Gui.blit(pose, x, y, 42, 0, 22, 16);
    }

    @Override
    public void drawCraftingBackground(PoseStack pose, int x, int y, int width, int height) {
        RenderUtils.bindTexture(CRAFTING);
        Gui.blitNineSliced(pose, x, y, width, height, 4, 4, 4, 4, 24, 24, 0, 0);
    }
}
