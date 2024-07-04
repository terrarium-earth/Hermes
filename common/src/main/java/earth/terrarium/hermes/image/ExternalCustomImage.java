package earth.terrarium.hermes.image;

import com.mojang.blaze3d.platform.NativeImage;
import earth.terrarium.hermes.renderer.HermesRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;

import java.awt.image.BufferedImage;
import java.io.Closeable;

public record ExternalCustomImage(ResourceLocation texture) implements CustomImage, Closeable {

    public ExternalCustomImage(BufferedImage image) {
        this(Minecraft.getInstance().getTextureManager().register(
                "hermes",
                new DynamicTexture(fromBuffered(image))
        ));
    }

    private static NativeImage fromBuffered(BufferedImage image) {
        NativeImage nativeImage = new NativeImage(image.getWidth(), image.getHeight(), true);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int argb = image.getRGB(x, y);
                int abgr = (argb & 0xFF00FF00) | ((argb & 0xFF) << 16) | ((argb >> 16) & 0xFF);
                nativeImage.setPixelRGBA(x, y, abgr);
            }
        }
        return nativeImage;
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
