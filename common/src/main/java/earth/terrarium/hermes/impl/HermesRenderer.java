package earth.terrarium.hermes.impl;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.teamresourceful.resourcefullib.client.CloseablePoseStack;
import com.teamresourceful.resourcefullib.client.screens.CursorScreen;
import earth.terrarium.hermes.api.rendering.HtmlRenderer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

import java.util.function.Consumer;

public final class HermesRenderer implements HtmlRenderer {

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
    public float width(String text, float scale) {
        return this.font.width(text) * scale;
    }

    @Override
    public void drawString(String text, float x, float y, float scale, int color, boolean shadow) {
        try (var stack = new CloseablePoseStack(graphics)) {
            stack.scale(scale, scale, 1f);
            this.graphics.drawString(
                    this.font,
                    text,
                    (int) (x / scale),
                    (int) (y / scale),
                    color,
                    shadow
            );
        }
    }

    @Override
    public void fill(float x, float y, float width, float height, int color) {
        this.graphics.fill((int) x, (int) y, (int) x + (int) width, (int) y + (int) height, color);
    }

    @Override
    public void blit(ResourceLocation texture, float x, float y, float width, float height) {
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.enableBlend();
        Matrix4f matrix = graphics.pose().last().pose();
        BufferBuilder buffer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        buffer.addVertex(matrix, x, y, 0f).setUv(0f, 0f);
        buffer.addVertex(matrix, x, y + height, 0f).setUv(0f, 1f);
        buffer.addVertex(matrix, x + width, y + height, 0f).setUv(1f, 1f);
        buffer.addVertex(matrix, x + width, y, 0f).setUv(1f, 0f);
        BufferUploader.drawWithShader(buffer.buildOrThrow());
        RenderSystem.disableBlend();
    }

    @Override
    public Font getFont() {
        return font;
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

}
