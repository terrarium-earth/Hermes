package earth.terrarium.hermes.api.defaults;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import earth.terrarium.hermes.api.Alignable.Alignment;
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

public class ImageTagElement implements TagElement {

    private final ResourceLocation image;
    private final int imageWidth;
    private final int imageHeight;
    private final int imageU;
    private final int imageV;
    private final int imageTextureWidth;
    private final int imageTextureHeight;
    private final Alignment align;

    public ImageTagElement(Map<String, String> parameters) {
        this.image = ElementParsingUtils.parseResourceLocation(parameters, "src", new ResourceLocation("textures/missing_no.png"));
        this.imageWidth = ElementParsingUtils.parseInt(parameters, "width", -1);
        this.imageHeight = ElementParsingUtils.parseInt(parameters, "height", -1);
        this.imageU = ElementParsingUtils.parseInt(parameters, "u", 0);
        this.imageV = ElementParsingUtils.parseInt(parameters, "v", 0);
        this.imageTextureWidth = ElementParsingUtils.parseInt(parameters, "textureWidth", -1);
        this.imageTextureHeight = ElementParsingUtils.parseInt(parameters, "textureHeight", -1);
        this.align = ElementParsingUtils.parseAlignment(parameters, "align", Alignment.CENTER);
    }

    @Override
    public void render(Theme theme, GuiGraphics graphics, int x, int y, int width, int mouseX, int mouseY, boolean hovered, float partialTicks) {
        if (this.imageTextureWidth == -1 && this.imageTextureHeight == -1) {
            AbstractTexture texture = Minecraft.getInstance().getTextureManager().getTexture(this.image);
            GlStateManager._bindTexture(texture.getId());
            int imageWidth = GlStateManager._getTexLevelParameter(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
            int imageHeight = GlStateManager._getTexLevelParameter(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);

            int fullWidth = this.imageWidth == -1 ? imageWidth : this.imageWidth;
            int fullHeight = this.imageHeight == -1 ? imageHeight : this.imageHeight;

            int xOffset = switch (align) {
                case LEFT -> (fullWidth / 2);
                case CENTER -> (width - fullWidth) / 2;
                case RIGHT -> (width - fullWidth);
            };

            blit(graphics, x + xOffset, y + 2, fullWidth, fullHeight, texture.getId());
        } else {
            int xOffset = (width - this.imageWidth) / 2;
            graphics.blit(this.image, x + xOffset, y + 2, this.imageU, this.imageV, this.imageWidth, this.imageHeight, this.imageTextureWidth, this.imageTextureHeight);
        }
    }

    private static void blit(GuiGraphics graphics, int x, int y, int width, int height, int texture) {
        RenderSystem.setShaderTexture(0, texture);
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
        return this.imageHeight + 4;
    }
}
