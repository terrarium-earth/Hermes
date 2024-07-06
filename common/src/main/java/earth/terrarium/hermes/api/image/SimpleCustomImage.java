package earth.terrarium.hermes.api.image;

import earth.terrarium.hermes.api.rendering.HtmlRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector4f;

public interface SimpleCustomImage extends CustomImage {

    ResourceLocation texture();

    @Override
    default void drawImage(float x, float y, float width, float height, @Nullable Vector4f radius, HtmlRenderer renderer) {
        renderer.blit(texture(), x, y, width, height, radius);
    }
}
