package earth.terrarium.hermes.impl.image.external;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.Weigher;
import com.mojang.logging.LogUtils;
import com.teamresourceful.resourcefullib.common.utils.WebUtils;
import dev.dediamondpro.minemark.providers.ImageProvider;
import earth.terrarium.hermes.api.image.CustomImage;
import earth.terrarium.hermes.api.image.ExternalImage;
import org.slf4j.Logger;

import javax.imageio.ImageIO;
import java.net.URI;
import java.net.http.HttpResponse;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ExternalImageProvider implements ImageProvider<CustomImage> {

    public static final ExternalImageProvider INSTANCE = new ExternalImageProvider();
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Set<String> SUPPORTED_SCHEMES = Set.of("http", "https");
    private static final Cache<String, ExternalImage> CACHE = CacheBuilder.newBuilder()
            .maximumWeight(50L * 1024L * 1024L) // 50 MB
            .weigher((Weigher<String, ExternalImage>) (key, value) -> value.size())
            .removalListener(notification -> notification.getValue().close())
            .build();

    @Override
    public void getImage(String url, Consumer<Dimension> dimensionsUploader, Consumer<CustomImage> imageUploader) {
        ExternalImage image = CACHE.getIfPresent(url);
        URI uri = createURI(url);
        if (image != null) {
            image.create(dimensionsUploader, imageUploader);
        } else if (uri != null && SUPPORTED_SCHEMES.contains(uri.getScheme())) {
            CompletableFuture.runAsync(() -> {
                try {
                    var i = startDownload(uri);
                    if (i != null) {
                        CACHE.put(url, i);
                        i.create(dimensionsUploader, imageUploader);
                    }
                } catch (Exception e) {
                    LOGGER.error("Failed to load image from URL: {}", url, e);
                }
            });
        }
    }

    private ExternalImage startDownload(URI uri) throws Exception {
        var response = WebUtils.get(uri.toString(), HttpResponse.BodyHandlers.ofInputStream()).orElse(null);
        if (response == null || response.statusCode() != 200) return null;
        var stream = ImageIO.createImageInputStream(response.body());
        var readers = ImageIO.getImageReaders(stream);
        if (readers.hasNext()) {
            var reader = readers.next();
            reader.setInput(stream, false);
            return switch (reader.getFormatName()) {
                case "gif" -> GifExternalImage.of(reader);
                case "png", "jpg", "jpeg" -> PngExternalImage.of(reader.read(0));
                case null, default -> null;
            };
        }
        return null;
    }

    private static URI createURI(String url) {
        try {
            return new URI(url);
        } catch (Exception e) {
            return null;
        }
    }
}
