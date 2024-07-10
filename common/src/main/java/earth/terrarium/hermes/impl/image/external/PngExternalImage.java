package earth.terrarium.hermes.impl.image.external;

import dev.dediamondpro.minemark.providers.ImageProvider;
import earth.terrarium.hermes.api.image.CustomImage;
import earth.terrarium.hermes.api.image.ExternalImage;
import earth.terrarium.hermes.api.image.SimpleCustomImage;
import earth.terrarium.hermes.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;

import java.awt.image.BufferedImage;
import java.util.function.Consumer;

public record PngExternalImage(int width, int height, BufferedImage image) implements ExternalImage {

    public static PngExternalImage of(BufferedImage image) {
        return new PngExternalImage(image.getWidth(), image.getHeight(), image);
    }

    @Override
    public void create(Consumer<ImageProvider.Dimension> dimensionsUploader, Consumer<CustomImage> imageUploader) {
        dimensionsUploader.accept(new ImageProvider.Dimension(width, height));
        imageUploader.accept(new ExternalCustomImage(image));
    }

    @Override
    public int size() {
        return width * height * Integer.BYTES;
    }

    public record ExternalCustomImage(ResourceLocation texture) implements SimpleCustomImage {

        public ExternalCustomImage(BufferedImage image) {
            this(Minecraft.getInstance().getTextureManager().register("hermes", new DynamicTexture(Utils.toNative(image))));
        }

        @Override
        public void close() {
            Minecraft.getInstance().getTextureManager().release(texture);
        }
    }
}
