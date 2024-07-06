package earth.terrarium.hermes.api.image;

import earth.terrarium.hermes.api.rendering.HtmlRenderer;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector4f;

import java.io.Closeable;

public interface CustomImage extends Closeable {

    void drawImage(float x, float y, float width, float height, @Nullable Vector4f radius, HtmlRenderer renderer);

    @Override
    default void close() {

    }
}
