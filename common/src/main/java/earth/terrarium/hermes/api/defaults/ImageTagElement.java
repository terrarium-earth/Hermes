package earth.terrarium.hermes.api.defaults;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import earth.terrarium.hermes.api.Alignment;
import earth.terrarium.hermes.api.TagElement;
import earth.terrarium.hermes.api.themes.Theme;
import earth.terrarium.hermes.utils.ElementParsingUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.util.Map;

public class ImageTagElement extends FillAndBorderElement implements TagElement {

    private final ResourceLocation image;
    private final AbstractTexture texture;
    private final int imageWidth;
    private final int imageHeight;
    private final int imageU;
    private final int imageV;
    private final int imageTextureWidth;
    private final int imageTextureHeight;
    private final Alignment align;

    public ImageTagElement(Map<String, String> parameters) {
        super(parameters);

        this.image = ElementParsingUtils.parseResourceLocation(parameters, "src", new ResourceLocation("textures/missing_no.png"));
        this.texture = Minecraft.getInstance().getTextureManager().getTexture(this.image);
        GlStateManager._bindTexture(texture.getId());
        int imageWidthGL = GlStateManager._getTexLevelParameter(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
        int imageHeightGL = GlStateManager._getTexLevelParameter(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);
        this.imageWidth = ElementParsingUtils.parseInt(parameters, "width", imageWidthGL);
        this.imageHeight = ElementParsingUtils.parseInt(parameters, "height", imageHeightGL);
        this.imageU = ElementParsingUtils.parseInt(parameters, "u", 0);
        this.imageV = ElementParsingUtils.parseInt(parameters, "v", 0);
        this.imageTextureWidth = ElementParsingUtils.parseInt(parameters, "textureWidth", -1);
        this.imageTextureHeight = ElementParsingUtils.parseInt(parameters, "textureHeight", -1);
        this.align = ElementParsingUtils.parseAlignment(parameters, "align", Alignment.MIDDLE);
    }

    @Override
    public void render(Theme theme, GuiGraphics graphics, int x, int y, int width, int mouseX, int mouseY, boolean hovered, float partialTicks) {
        int xOffset = Alignment.getOffset(width, this.imageWidth, align);
        int yOffset = verticalSpacing;
        drawBackground(graphics, x + xOffset, y + yOffset, imageWidth, imageHeight);

        if (this.imageTextureWidth == -1 && this.imageTextureHeight == -1) {
            blit(graphics, x + xOffset, y + yOffset, imageWidth, imageHeight);
        } else {
            graphics.blit(this.image,
                    x + xOffset, y + yOffset,
                    this.imageU, this.imageV,
                    this.imageWidth, this.imageHeight,
                    imageTextureWidth, imageTextureHeight);
        }
    }

    private void blit(GuiGraphics graphics, int x, int y, int width, int height) {
        RenderSystem.setShaderTexture(0, texture.getId());
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        Matrix4f matrix4f = graphics.pose().last().pose();
        BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferBuilder.vertex(matrix4f, (float)x, (float)y, 0f).uv(0f, 0f).endVertex();
        bufferBuilder.vertex(matrix4f, (float)x, (float)y + height, 0f).uv(0f, 1f).endVertex();
        bufferBuilder.vertex(matrix4f, (float)x + width, (float)y + height, 0f).uv(1f, 1f).endVertex();
        bufferBuilder.vertex(matrix4f, (float)x + width, (float)y, 0f).uv(1f, 0f).endVertex();

        BufferUploader.drawWithShader(bufferBuilder.end());
    }

    @Override
    public int getHeight(int width) {
        return this.imageHeight + (2 * verticalSpacing);
    }
}
