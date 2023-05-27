package earth.terrarium.hermes.api.defaults;

import earth.terrarium.hermes.api.TagElement;
import earth.terrarium.hermes.api.themes.Theme;
import earth.terrarium.hermes.utils.ElementParsingUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class ImageTagElement implements TagElement {

    private final ResourceLocation image;
    private final int imageWidth;
    private final int imageHeight;
    private final int imageU;
    private final int imageV;
    private final int imageTextureWidth;
    private final int imageTextureHeight;

    public ImageTagElement(Map<String, String> parameters) {
        this.image = ElementParsingUtils.parseResourceLocation(parameters, "src", new ResourceLocation("textures/missing_no.png"));
        this.imageWidth = ElementParsingUtils.parseInt(parameters, "width", 0);
        this.imageHeight = ElementParsingUtils.parseInt(parameters, "height", 0);
        this.imageU = ElementParsingUtils.parseInt(parameters, "u", 0);
        this.imageV = ElementParsingUtils.parseInt(parameters, "v", 0);
        this.imageTextureWidth = ElementParsingUtils.parseInt(parameters, "textureWidth", this.imageWidth);
        this.imageTextureHeight = ElementParsingUtils.parseInt(parameters, "textureHeight", this.imageHeight);
    }

    @Override
    public void render(Theme theme, GuiGraphics graphics, int x, int y, int width, int mouseX, int mouseY, boolean hovered, float partialTicks) {
        int xOffset = (width - this.imageWidth) / 2;
        graphics.blit(this.image, x + xOffset, y + 2, this.imageU, this.imageV, this.imageWidth, this.imageHeight, this.imageTextureWidth, this.imageTextureHeight);
    }

    @Override
    public int getHeight(int width) {
        return this.imageHeight + 4;
    }
}
