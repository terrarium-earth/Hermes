package earth.terrarium.hermes.impl.image;

import earth.terrarium.hermes.api.image.CustomImage;
import earth.terrarium.hermes.api.rendering.HtmlRenderer;
import net.minecraft.resources.ResourceLocation;

public record InternalCustomImage(ResourceLocation texture) implements CustomImage {

    @Override
    public void drawImage(float x, float y, float width, float height, HtmlRenderer renderer) {
        renderer.blit(texture, x, y, width, height);
    }

}
