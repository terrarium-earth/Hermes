package earth.terrarium.hermes.api.themes;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teamresourceful.resourcefullib.client.utils.RenderUtils;
import com.teamresourceful.resourcefullib.common.color.ConstantColors;
import earth.terrarium.hermes.Hermes;
import earth.terrarium.hermes.utils.Divisor;
import it.unimi.dsi.fastutil.ints.IntIterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class DefaultTheme implements Theme {

    private static final ResourceLocation DETAILS = new ResourceLocation(Hermes.MOD_ID, "textures/gui/details.png");
    private static final ResourceLocation CRAFTING = new ResourceLocation(Hermes.MOD_ID, "textures/gui/crafting.png");
    private static final ResourceLocation CAROUSEL = new ResourceLocation(Hermes.MOD_ID, "textures/gui/carousel.png");

    @Override
    public void drawDropdown(PoseStack pose, int x, int y, int width, boolean hovered, boolean open, String text) {
        RenderUtils.bindTexture(DETAILS);
        int vOffset = hovered ? 20 : 0;
        Gui gui = Minecraft.getInstance().gui;
        gui.blit(pose, x, y, 0, vOffset, 2, 20);
        blitRepeating(gui, pose, x + 2, y, width - 4, 20, 2, vOffset, 196, 20);
        gui.blit(pose, x + width - 2, y, 198, vOffset, 2, 20);
        if (open) {
            gui.blit(pose, x + 4, y + 6, 0, 40, 11, 7);
        } else {
            gui.blit(pose, x + 7, y + 4, 11, 40, 7, 11);
        }
        Minecraft.getInstance().font.draw(pose, text, x + 20, y + 6, ConstantColors.white.getValue());
    }

    public static void blitRepeating(Gui gui, PoseStack poseStack, int i, int j, int k, int l, int m, int n, int o, int p) {
        int q = i;

        int r;
        for(IntIterator intIterator = slices(k, o); intIterator.hasNext(); q += r) {
            r = intIterator.nextInt();
            int s = (o - r) / 2;
            int t = j;

            int u;
            for(IntIterator intIterator2 = slices(l, p); intIterator2.hasNext(); t += u) {
                u = intIterator2.nextInt();
                int v = (p - u) / 2;
                gui.blit(poseStack, q, t, m + s, n + v, r, u);
            }
        }

    }

    private static IntIterator slices(int i, int j) {
        int k = Mth.positiveCeilDiv(i, j);
        return new Divisor(i, k);
    }

    public static void blitNineSliced(Gui gui, PoseStack poseStack, int i, int j, int k, int l, int m, int n, int o, int p, int q, int r, int s, int t) {
        m = Math.min(m, k / 2);
        o = Math.min(o, k / 2);
        n = Math.min(n, l / 2);
        p = Math.min(p, l / 2);
        if (k == q && l == r) {
            gui.blit(poseStack, i, j, s, t, k, l);
        } else if (l == r) {
            gui.blit(poseStack, i, j, s, t, m, l);
            blitRepeating(gui, poseStack, i + m, j, k - o - m, l, s + m, t, q - o - m, r);
            gui.blit(poseStack, i + k - o, j, s + q - o, t, o, l);
        } else if (k == q) {
            gui.blit(poseStack, i, j, s, t, k, n);
            blitRepeating(gui, poseStack, i, j + n, k, l - p - n, s, t + n, q, r - p - n);
            gui.blit(poseStack, i, j + l - p, s, t + r - p, k, p);
        } else {
            gui.blit(poseStack, i, j, s, t, m, n);
            blitRepeating(gui, poseStack, i + m, j, k - o - m, n, s + m, t, q - o - m, n);
            gui.blit(poseStack, i + k - o, j, s + q - o, t, o, n);
            gui.blit(poseStack, i, j + l - p, s, t + r - p, m, p);
            blitRepeating(gui, poseStack, i + m, j + l - p, k - o - m, p, s + m, t + r - p, q - o - m, p);
            gui.blit(poseStack, i + k - o, j + l - p, s + q - o, t + r - p, o, p);
            blitRepeating(gui, poseStack, i, j + n, m, l - p - n, s, t + n, m, r - p - n);
            blitRepeating(gui, poseStack, i + m, j + n, k - o - m, l - p - n, s + m, t + n, q - o - m, r - p - n);
            blitRepeating(gui, poseStack, i + k - o, j + n, m, l - p - n, s + q - o, t + n, o, r - p - n);
        }
    }

    @Override
    public void drawSlot(PoseStack pose, int x, int y, boolean hovered) {
        RenderUtils.bindTexture(CRAFTING);
        Minecraft.getInstance().gui.blit(pose, x, y, 24, 0, 18, 18);
        if (hovered) {
            Gui.fill(pose, x + 1, y + 1, x + 17, y + 17, 0x80FFFFFF);
        }
    }

    @Override
    public void drawArrow(PoseStack pose, int x, int y) {
        RenderUtils.bindTexture(CRAFTING);
        Minecraft.getInstance().gui.blit(pose, x, y, 42, 0, 22, 16);
    }

    @Override
    public void drawCraftingBackground(PoseStack pose, int x, int y, int width, int height) {
        RenderUtils.bindTexture(CRAFTING);
        blitNineSliced(Minecraft.getInstance().gui, pose, x, y, width, height, 4, 4, 4, 4, 24, 24, 0, 0);
    }

    @Override
    public void drawCarouselButton(PoseStack pose, int x, int y, boolean left, boolean hovered) {
        RenderUtils.bindTexture(CAROUSEL);
        Minecraft.getInstance().gui.blit(pose, x, y, left ? 0 : 14, hovered ? 20 : 0, 14, 20);
    }
}
