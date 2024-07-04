package earth.terrarium.hermes.api.image;

import earth.terrarium.hermes.api.rendering.HtmlRenderer;

import java.io.Closeable;

public interface CustomImage extends Closeable {

    void drawImage(float x, float y, float width, float height, HtmlRenderer renderer);

    @Override
    default void close() {

    }
}
