package earth.terrarium.hermes.image;

import earth.terrarium.hermes.renderer.HermesRenderer;
import net.minecraft.resources.ResourceLocation;

public record InternalCustomImage(ResourceLocation texture) implements CustomImage {

    @Override
    public void drawImage(float x, float y, float width, float height, HermesRenderer renderer) {
        renderer.blit(texture, x, y, width, height);
    }

}
