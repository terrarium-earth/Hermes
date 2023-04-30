package earth.terrarium.hermes.api.themes;

import com.mojang.blaze3d.vertex.PoseStack;

public interface Theme {

    void drawDropdown(PoseStack pose, int x, int y, int width, boolean hovered, boolean open, String text);

    void drawSlot(PoseStack pose, int x, int y, boolean hovered);

    void drawArrow(PoseStack pose, int x, int y);

    void drawCraftingBackground(PoseStack pose, int x, int y, int width, int height);

}
