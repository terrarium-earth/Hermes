package earth.terrarium.hermes.image;

import earth.terrarium.hermes.renderer.HermesRenderer;

import java.io.Closeable;

public interface CustomImage extends Closeable {

    void drawImage(float x, float y, float width, float height, HermesRenderer renderer);

    @Override
    default void close() {

    }
}
