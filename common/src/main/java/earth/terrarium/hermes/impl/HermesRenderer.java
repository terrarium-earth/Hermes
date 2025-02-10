package earth.terrarium.hermes.impl;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.teamresourceful.resourcefullib.client.CloseablePoseStack;
import com.teamresourceful.resourcefullib.client.screens.CursorScreen;
import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.utils.ColorFactory;
import earth.terrarium.hermes.api.rendering.HtmlRenderer;
import earth.terrarium.hermes.elements.html.GlobalAttributesElement;
import earth.terrarium.hermes.mixin.FontManagerAccessor;
import earth.terrarium.hermes.mixin.MinecraftAccessor;
import earth.terrarium.hermes.shader.impl.RoundedRectShader;
import earth.terrarium.hermes.shader.impl.RoundedTextureShader;
import earth.terrarium.hermes.css.Border;
import earth.terrarium.hermes.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.awt.*;
import java.util.function.Consumer;

public final class HermesRenderer implements HtmlRenderer {

    private static final Font MONOSPACED = new Font(
            id -> ((FontManagerAccessor) ((MinecraftAccessor) Minecraft.getInstance()).getFontManager())
                    .getFontSets().get(ResourceLocation.fromNamespaceAndPath("hermes", "monospaced")),
            false
    );

    private final Font font;
    private final GuiGraphics graphics;
    private final Consumer<CursorScreen.Cursor> cursorSetter;

    private Component tooltip;

    public HermesRenderer(Font font, GuiGraphics graphics, Consumer<CursorScreen.Cursor> cursorSetter) {
        this.font = font;
        this.graphics = graphics;
        this.cursorSetter = cursorSetter;
    }

    @Override
    public float width(String text, float scale, boolean monospaced) {
        if (monospaced) return MONOSPACED.width(text) * scale;
        return this.font.width(text) * scale;
    }

    @Override
    public void drawString(String text, float x, float y, float scale, int color, boolean shadow, boolean monospaced) {
        try (var stack = new CloseablePoseStack(graphics)) {
            stack.scale(scale, scale, 1f);
            this.graphics.drawString(
                    monospaced ? MONOSPACED : this.font,
                    text,
                    (int) (x / scale),
                    (int) (y / scale),
                    color,
                    shadow
            );
        }
    }

    @Override
    public void fill(float x, float y, float width, float height, int backgroundColor, int borderColor, float borderWidth, Vector4f borderRadius) {
        if (borderRadius == null && borderWidth == 0f) {
            this.graphics.fill((int) x, (int) y, (int) x + (int) width, (int) y + (int) height, backgroundColor);
        } else {
            Window window = Minecraft.getInstance().getWindow();
            float scale = (float) window.getGuiScale();
            float scaledHeight = height * scale;
            float scaledWidth = width * scale;
            float xScale = x * scale;
            float yScale = y * scale;

            float yOffset = (window.getScreenHeight() - scaledHeight) - (yScale * 2f);

            RenderSystem.enableBlend();
            RoundedRectShader.use(
                    new Matrix4f(RenderSystem.getModelViewMatrix()),
                    new Matrix4f(RenderSystem.getProjectionMatrix()),
                    Utils.toVec4f(backgroundColor),
                    Utils.toVec4f(borderColor),
                    borderRadius,
                    borderWidth,
                    new Vector2f(scaledWidth - (borderWidth * 2f * scale), scaledHeight - (borderWidth * 2f * scale)),
                    new Vector2f(
                            xScale + ((width * scale) / 2f),
                            yScale + ((height * scale) / 2f) + yOffset
                    ),
                    scale
            );

            Matrix4f matrix = graphics.pose().last().pose();

            BufferBuilder buffer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
            buffer.addVertex(matrix, x, y, 0f);
            buffer.addVertex(matrix, x, y + height, 0f);
            buffer.addVertex(matrix, x + width, y + height, 0f);
            buffer.addVertex(matrix, x + width, y, 0f);
            BufferUploader.draw(buffer.buildOrThrow());

            RoundedRectShader.unuse();
            RenderSystem.disableBlend();
        }
    }

    @Override
    public void blit(
            ResourceLocation texture,
            float x, float y,
            float u0, float v0, float u1, float v1,
            float width, float height,
            Vector4f borderRadius
    ) {
        RenderSystem.enableBlend();

        if (borderRadius == null) {
            RenderSystem.setShaderTexture(0, texture);
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
        } else {
            Window window = Minecraft.getInstance().getWindow();
            float scale = (float) window.getGuiScale();
            float scaledHeight = height * scale;
            float scaledWidth = width * scale;
            float xScale = x * scale;
            float yScale = y * scale;

            float yOffset = (window.getScreenHeight() - scaledHeight) - (yScale * 2f);

            RoundedTextureShader.use(
                    new Matrix4f(RenderSystem.getModelViewMatrix()),
                    new Matrix4f(RenderSystem.getProjectionMatrix()),
                    texture,
                    borderRadius,
                    new Vector2f(scaledWidth, scaledHeight),
                    new Vector2f(
                            xScale + ((width * scale) / 2f),
                            yScale + ((height * scale) / 2f) + yOffset
                    ),
                    scale
            );
            AbstractTexture textureObj = Minecraft.getInstance().getTextureManager().getTexture(texture);
            RenderSystem.bindTexture(textureObj.getId());
        }

        Matrix4f matrix = graphics.pose().last().pose();
        BufferBuilder buffer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        buffer.addVertex(matrix, x, y, 0f).setUv(u0, v0);
        buffer.addVertex(matrix, x, y + height, 0f).setUv(u0, v1);
        buffer.addVertex(matrix, x + width, y + height, 0f).setUv(u1, v1);
        buffer.addVertex(matrix, x + width, y, 0f).setUv(u1, v0);

        if (borderRadius == null) {
            BufferUploader.drawWithShader(buffer.buildOrThrow());
        } else {
            BufferUploader.draw(buffer.buildOrThrow());
            RoundedTextureShader.unuse();
        }
        RenderSystem.disableBlend();
    }

    @Override
    public void blit(int texture, float x, float y, float u0, float v0, float u1, float v1, float width, float height) {
        RenderSystem.enableBlend();

        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);

        Matrix4f matrix = graphics.pose().last().pose();
        BufferBuilder buffer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        buffer.addVertex(matrix, x, y, 0f).setUv(u0, v0);
        buffer.addVertex(matrix, x, y + height, 0f).setUv(u0, v1);
        buffer.addVertex(matrix, x + width, y + height, 0f).setUv(u1, v1);
        buffer.addVertex(matrix, x + width, y, 0f).setUv(u1, v0);

        BufferUploader.drawWithShader(buffer.buildOrThrow());
        RenderSystem.disableBlend();
    }

    @Override
    public Font getFont() {
        return font;
    }

    @Override
    public Font getMonospacedFont() {
        return MONOSPACED;
    }

    @Override
    public GuiGraphics getGraphics() {
        return graphics;
    }

    @Override
    public void setTooltip(Component text) {
        this.tooltip = text;
    }

    @Override
    public Component getTooltip() {
        return tooltip;
    }

    @Override
    public void setCursor(CursorScreen.Cursor cursor) {
        cursorSetter.accept(cursor);
    }

    public static void drawDefault(float x, float y, float width, float height, LayoutStyle style, HtmlRenderer renderer) {
        drawDefault(x, y, width, height, ColorFactory.TRANSPARENT, style, renderer);
    }

    public static void drawDefault(float x, float y, float width, float height, Color borderFallback, LayoutStyle style, HtmlRenderer renderer) {
        Border border = style.getOrDefault(GlobalAttributesElement.BORDER, Border.NONE);
        Color backgroundColor = style.getOrDefault(GlobalAttributesElement.BACKGROUND_COLOR, ColorFactory.TRANSPARENT);

        renderer.fill(
                x, y, width, height,
                backgroundColor.getRGB(), border.getColor(borderFallback),
                border.getWidth(width, height),
                border.getRadius(width, height)
        );
    }
}
