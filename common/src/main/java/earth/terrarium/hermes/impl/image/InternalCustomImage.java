package earth.terrarium.hermes.impl.image;

import earth.terrarium.hermes.api.image.SimpleCustomImage;
import net.minecraft.resources.ResourceLocation;

public record InternalCustomImage(ResourceLocation texture) implements SimpleCustomImage {

}
