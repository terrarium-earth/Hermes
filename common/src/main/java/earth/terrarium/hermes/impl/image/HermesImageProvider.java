package earth.terrarium.hermes.impl.image;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.dediamondpro.minemark.providers.ImageProvider;
import earth.terrarium.hermes.api.image.CustomImage;
import earth.terrarium.hermes.impl.image.external.ExternalImageProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.function.Consumer;

public final class HermesImageProvider implements ImageProvider<CustomImage> {

    public static final HermesImageProvider INSTANCE = new HermesImageProvider();

    @Override
    public void getImage(String src, Consumer<Dimension> dimensionCallback, Consumer<CustomImage> imageCallback) {
        if (src.startsWith("https://") || src.startsWith("http://") || src.startsWith("data:image/")) {
            ExternalImageProvider.INSTANCE.getImage(src, dimensionCallback, imageCallback);
        } else {
            ResourceLocation id = ResourceLocation.tryParse(src);
            if (id == null) return;
            if (id.getNamespace().equals("minecraft") && !src.startsWith("minecraft:")) return;
            GlStateManager._bindTexture(Minecraft.getInstance().getTextureManager().getTexture(id).getId());
            dimensionCallback.accept(new Dimension(
                    GlStateManager._getTexLevelParameter(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH),
                    GlStateManager._getTexLevelParameter(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT)
            ));
            imageCallback.accept(new InternalCustomImage(id));
        }
    }
}
