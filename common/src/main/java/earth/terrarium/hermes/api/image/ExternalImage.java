package earth.terrarium.hermes.api.image;

import dev.dediamondpro.minemark.providers.ImageProvider;

import java.io.Closeable;
import java.util.function.Consumer;

public interface ExternalImage extends Closeable {

    void create(Consumer<ImageProvider.Dimension> dimensionsUploader, Consumer<CustomImage> imageUploader);

    int size();

    @Override
    default void close() {

    }
}
