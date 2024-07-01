package earth.terrarium.hermes.image;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.Weigher;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.logging.LogUtils;
import com.teamresourceful.resourcefullib.common.utils.WebUtils;
import dev.dediamondpro.minemark.providers.ImageProvider;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.slf4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public final class HermesImageProvider implements ImageProvider<CustomImage> {

    public static final HermesImageProvider INSTANCE = new HermesImageProvider();
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Cache<String, BufferedImage> CACHE = CacheBuilder.newBuilder()
            .maximumWeight(50 * 1024 * 1024)
            .weigher((Weigher<String, BufferedImage>) (key, value) -> value.getHeight() * value.getWidth())
            .build();

    @Override
    public void getImage(String src, Consumer<Dimension> dimensionCallback, Consumer<CustomImage> imageCallback) {
        if (src.startsWith("https://") || src.startsWith("http://")) {
            BufferedImage bufferedImage = CACHE.getIfPresent(src);
            if (bufferedImage != null) {
                try {
                    load(src, bufferedImage, dimensionCallback, imageCallback);
                } catch (Exception e) {
                    LOGGER.error("Failed to load image from URL: {}", src, e);
                }
                return;
            }
            CompletableFuture.runAsync(() -> WebUtils.get(src, HttpResponse.BodyHandlers.ofInputStream())
                        .ifPresent(response -> {
                            if (response.statusCode() != 200) return;
                            try {
                                BufferedImage image = ImageIO.read(response.body());
                                CACHE.put(src, image);
                                load(src, image, dimensionCallback, imageCallback);
                            } catch (Exception e) {
                                LOGGER.error("Failed to load image from URL: {}", src, e);
                            }
                        })
            , Util.ioPool());
        } else {
            ResourceLocation id = ResourceLocation.tryParse(src);
            if (id == null) return;
            GlStateManager._bindTexture(Minecraft.getInstance().getTextureManager().getTexture(id).getId());
            dimensionCallback.accept(new Dimension(
                    GlStateManager._getTexLevelParameter(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH),
                    GlStateManager._getTexLevelParameter(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT)
            ));
            imageCallback.accept(new InternalCustomImage(id));
        }
    }

    private static void load(String url, BufferedImage image, Consumer<Dimension> dimensionCallback, Consumer<CustomImage> imageCallback) {
        try {
            NativeImage nativeImage = fromBuffered(image);
            Dimension dimension = new Dimension(nativeImage.getWidth(), nativeImage.getHeight());
            ExternalCustomImage customImage = new ExternalCustomImage(nativeImage);
            dimensionCallback.accept(dimension);
            imageCallback.accept(customImage);
        } catch (Exception e) {
            LOGGER.error("Failed to load image from URL: {}", url, e);
        }
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
}
