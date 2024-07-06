package earth.terrarium.hermessvg;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.Weigher;
import com.mojang.logging.LogUtils;
import dev.dediamondpro.minemark.providers.ImageProvider;
import earth.terrarium.hermes.api.image.CustomImage;
import earth.terrarium.hermes.impl.image.HermesImageProvider;
import net.minecraft.Util;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.TranscodingHints;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.slf4j.Logger;

import java.awt.image.BufferedImage;
import java.io.StringReader;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class SvgDecoder {

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Cache<String, BufferedImage> CACHE = CacheBuilder.newBuilder()
            .maximumWeight(50 * 1024 * 1024)
            .weigher((Weigher<String, BufferedImage>) (key, value) -> value.getHeight() * value.getWidth())
            .build();

    public static void decode(
            int width, int height,
            String data,
            Consumer<ImageProvider.Dimension> dimensionCallback,
            Consumer<CustomImage> imageCallback
    ) {
        BufferedImage bufferedImage = CACHE.getIfPresent(data);
        if (bufferedImage != null) {
            try {
                HermesImageProvider.load(bufferedImage, dimensionCallback, imageCallback);
            } catch (Exception e) {
                LOGGER.error("Failed to load image from svg: {}", data, e);
            }
            return;
        }

        CompletableFuture.runAsync(() -> {
            try (var reader = new StringReader(data)) {
                ImageTranscoder transcoder = new BufferedImageTranscoder(
                        image -> {
                            CACHE.put(data, image);
                            HermesImageProvider.load(image, dimensionCallback, imageCallback);
                        }
                );
                TranscodingHints hints = transcoder.getTranscodingHints();
                if (width > 0 && height > 0) {
                    hints.put(ImageTranscoder.KEY_WIDTH, width * 4f);
                    hints.put(ImageTranscoder.KEY_HEIGHT, height * 4f);
                }
                hints.put(ImageTranscoder.KEY_PIXEL_UNIT_TO_MILLIMETER, 0.352777778f);
                hints.put(ImageTranscoder.KEY_ALLOWED_SCRIPT_TYPES, "");
                hints.put(ImageTranscoder.KEY_ALLOW_EXTERNAL_RESOURCES, false);
                transcoder.setTranscodingHints(hints);
                transcoder.transcode(new TranscoderInput(reader), null);
            } catch (Exception e) {
                LOGGER.error("Failed to load image from svg: {}", data, e);
            }
        }, Util.ioPool());
    }

    private static class BufferedImageTranscoder extends ImageTranscoder {

        private final Consumer<BufferedImage> writer;

        public BufferedImageTranscoder(Consumer<BufferedImage> writer) {
            this.writer = writer;
        }

        @Override
        public BufferedImage createImage(int width, int height) {
            return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        }

        @Override
        public void writeImage(BufferedImage img, TranscoderOutput output) {
            this.writer.accept(img);
        }
    }
}
