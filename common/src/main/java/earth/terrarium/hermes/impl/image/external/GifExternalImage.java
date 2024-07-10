package earth.terrarium.hermes.impl.image.external;

import dev.dediamondpro.minemark.providers.ImageProvider;
import earth.terrarium.hermes.api.image.CustomImage;
import earth.terrarium.hermes.api.image.ExternalImage;
import earth.terrarium.hermes.api.rendering.HtmlRenderer;
import earth.terrarium.hermes.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector4f;

import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadataNode;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.function.Consumer;

public class GifExternalImage implements ExternalImage {

    private static final int MAX_SIZE = 8 * 1024;

    private final int width;
    private final int height;
    private final int sheetWidth;
    private final int sheetHeight;
    private final Frame[] frames;
    private final ResourceLocation[] images;
    private int usages;
    private boolean canBeClosed;

    private GifExternalImage(int width, int height, Frame[] frames, BufferedImage[] images) {
        this.width = width;
        this.height = height;
        this.frames = frames;
        this.images = new ResourceLocation[images.length];
        for (int i = 0; i < images.length; i++) {
            this.images[i] = Minecraft.getInstance().getTextureManager().register(
                    "hermes",
                    new DynamicTexture(Utils.toNative(images[i]))
            );
        }
        this.sheetWidth = images[0].getWidth();
        this.sheetHeight = images[0].getHeight();
    }

    public static GifExternalImage of(ImageReader reader) throws IOException {
        var metadata = reader.getImageMetadata(0);
        int height = reader.getHeight(0);
        int width = reader.getWidth(0);
        if (height > MAX_SIZE || width > MAX_SIZE) throw new IOException("Gif frames too large");
        int maxWidth = (MAX_SIZE / width) * width;
        int maxHeight = (MAX_SIZE / height) * height;
        int countPerSheet = (maxWidth / width) * (maxHeight / height);
        String formatName = metadata.getNativeMetadataFormatName();

        int frameCount = reader.getNumImages(true);
        Frame[] frames = new Frame[frameCount];
        BufferedImage[] images = new BufferedImage[Math.ceilDiv(frameCount, countPerSheet)];
        for (int i = 0; i < images.length; i++) images[i] = new BufferedImage(maxWidth, maxHeight, BufferedImage.TYPE_4BYTE_ABGR);

        int frame = 0;
        int u = 0;
        int v = 0;

        for (int i = 0; i < frameCount; i++) {
            BufferedImage frameImage = images[frame];
            BufferedImage image = reader.read(i);
            frameImage.getGraphics().drawImage(image, u, v, null);
            frames[i] = new Frame(frame, u, v, getDelay(reader, i, formatName));
            u += width;
            if (u >= maxWidth) {
                u = 0;
                v += height;
            }
            if (v >= maxHeight) {
                v = 0;
                frame++;
            }
        }

        return new GifExternalImage(width, height, frames, images);
    }

    @Override
    public void create(Consumer<ImageProvider.Dimension> dimensionsUploader, Consumer<CustomImage> imageUploader) {
        dimensionsUploader.accept(new ImageProvider.Dimension(width, height));
        imageUploader.accept(new Image(this));
    }

    @Override
    public int size() {
        return frames.length * width * height * Integer.BYTES;
    }

    @Override
    public void close() {
        this.canBeClosed = true;
        if (this.usages == 0) {
            for (ResourceLocation image : this.images) {
                Minecraft.getInstance().getTextureManager().release(image);
            }
        }
    }

    /**
     * Taken from <a href="https://github.com/InnovativeOnlineIndustries/Emojiful/blob/1.20/Common/src/main/java/com/hrznstudio/emojiful/util/EmojiUtil.java#L75">EmojiUtil.java in Emojiful</a>
     */
    private static int getDelay(ImageReader reader, int index, String formatName) {
        try {
            int delay = 0;
            var root = (IIOMetadataNode) reader.getImageMetadata(index).getAsTree(formatName);
            for (int i = 0; i < root.getLength(); i++) {
                var node = root.item(i);
                if (node.getNodeName().equalsIgnoreCase("GraphicControlExtension")) {
                    delay = Integer.parseInt(node.getAttributes().getNamedItem("delayTime").getNodeValue());
                    break;
                }
            }
            return delay == 0 ? 15 : delay;
        } catch (Exception e) {
            return 15;
        }
    }

    private record Frame(int sheet, int u, int v, int delay) {}

    private static class Image implements CustomImage {

        private final GifExternalImage gif;
        private long start = System.currentTimeMillis() / 10;
        private int frame;

        public Image(GifExternalImage gif) {
            this.gif = gif;
            this.gif.usages++;
        }

        public int getFrame() {
            long duration = System.currentTimeMillis() / 10 - start;
            int delay = gif.frames[frame].delay;
            if (duration >= delay) {
                start = System.currentTimeMillis() / 10;
                frame++;
                if (frame >= gif.frames.length) frame = 0;
            }
            return frame;
        }

        @Override
        public void drawImage(float x, float y, float width, float height, @Nullable Vector4f radius, HtmlRenderer renderer) {
            int frame = getFrame();
            int sheet = gif.frames[frame].sheet;
            float u0 = (float) gif.frames[frame].u / gif.sheetWidth;
            float v0 = (float) gif.frames[frame].v / gif.sheetHeight;
            float u1 = (float) (gif.frames[frame].u + gif.width) / gif.sheetWidth;
            float v1 = (float) (gif.frames[frame].v + gif.height) / gif.sheetHeight;
            renderer.blit(gif.images[sheet], x, y, u0, v0, u1, v1, width, height, radius);
        }

        @Override
        public void close() {
            gif.usages--;
            if (gif.usages == 0 && gif.canBeClosed) {
                gif.close();
            }
        }
    }
}
