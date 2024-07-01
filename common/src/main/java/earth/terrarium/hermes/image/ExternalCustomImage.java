package earth.terrarium.hermes.image;

import com.mojang.blaze3d.platform.NativeImage;
import earth.terrarium.hermes.renderer.HermesRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;

import java.io.Closeable;

public record ExternalCustomImage(ResourceLocation texture) implements CustomImage, Closeable {

    public ExternalCustomImage(NativeImage image) {
        this(Minecraft.getInstance().getTextureManager().register("hermes", new DynamicTexture(image)));
    }

    @Override
    public void drawImage(float x, float y, float width, float height, HermesRenderer renderer) {
        renderer.blit(texture, x, y, width, height);
    }

    @Override
    public void close() {
        Minecraft.getInstance().getTextureManager().release(texture);
    }
}
