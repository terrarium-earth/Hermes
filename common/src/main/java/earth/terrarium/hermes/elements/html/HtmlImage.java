package earth.terrarium.hermes.elements.html;

import com.teamresourceful.resourcefullib.client.screens.CursorScreen;
import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.elements.impl.ImageElement;
import dev.dediamondpro.minemark.utils.MouseButton;
import earth.terrarium.hermes.Hermes;
import earth.terrarium.hermes.data.GlobalData;
import earth.terrarium.hermes.data.map.ImageMap;
import earth.terrarium.hermes.data.map.MapArea;
import earth.terrarium.hermes.image.CustomImage;
import earth.terrarium.hermes.renderer.HermesRenderer;
import earth.terrarium.hermes.styles.HermesStyle;
import net.minecraft.Optionull;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

import java.util.Set;

public class HtmlImage extends ImageElement<HermesStyle, HermesRenderer, CustomImage> {

    private static final ResourceLocation MISSING = ResourceLocation.fromNamespaceAndPath(
            Hermes.MOD_ID,
            "textures/gui/missing.png"
    );

    private final String map;

    public HtmlImage(@NotNull HermesStyle style, @NotNull LayoutStyle layoutStyle, @Nullable Element<HermesStyle, HermesRenderer> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);
        assert attributes != null;

        if (qName.equals("img")) {
            this.map = Optionull.map(
                    attributes.getValue("usemap"),
                    s -> s.startsWith("#") ? s.substring(1) : null
            );
        } else {
            this.map = null;
        }
    }

    public static HtmlImage forEmbed(@NotNull HermesStyle style, @NotNull LayoutStyle layoutStyle, @Nullable Element<HermesStyle, HermesRenderer> parent, @NotNull String qName, @NotNull Attributes attributes) {
        String type = attributes.getValue("type");
        if (type != null && type.startsWith("image/")) {
            return new HtmlImage(style, layoutStyle, parent, qName, attributes);
        }
        return null;
    }

    public static HtmlImage forObject(@NotNull HermesStyle style, @NotNull LayoutStyle layoutStyle, @Nullable Element<HermesStyle, HermesRenderer> parent, @NotNull String qName, @NotNull Attributes attributes) {
        String data = attributes.getValue("data");
        if (data == null || !data.contains(".")) return null;
        Set<String> imageTypes = Set.of(".png", ".jpg", ".jpeg", ".gif", ".bmp", ".webp");
        String extension = data.substring(data.lastIndexOf('.'));
        if (imageTypes.contains(extension)) {
            return new HtmlImage(style, layoutStyle, parent, qName, attributes);
        }
        return null;
    }

    @Override
    protected void drawElement(float x, float y, float width, float height, HermesRenderer renderer) {
        super.drawElement(x, y, width, height, renderer);
        if (this.image != null) return;
        if (this.width == -1 || this.height == -1) return;
        renderer.fill(x, y, x + width, 1, 0xFF808080);
        renderer.fill(x, y + height - 1, x + width, 1, 0xFF808080);
        renderer.fill(x, y, 1, height, 0xFF808080);
        renderer.fill(x + width - 1, y, 1, height, 0xFF808080);
        renderer.fill(x + 1, y + 1, width - 2, height - 2, 0x80808080);

        x = x + (width - 16) / 2;
        y = y + (height - 16) / 2;

        renderer.blit(MISSING, x, y, 16, 16);
    }

    @Override
    public void drawInternal(float xOffset, float yOffset, float mouseX, float mouseY, HermesRenderer renderData) {
        super.drawInternal(xOffset, yOffset, mouseX, mouseY, renderData);
        if (this.image == null) return;
        CursorScreen.Cursor cursor = this.layoutStyle.get(AttributesGlobal.CURSOR);
        if (cursor != null) {
            renderData.setCursor(cursor);
        } else {
            ImageMap imageMap = this.style.globalData().get(GlobalData.IMAGE_MAP, map);
            if (imageMap == null) return;
            MapArea area = imageMap.getArea(
                    (mouseX - position.getX()) * (imageWidth / width),
                    (mouseY - position.getY()) * (imageHeight / height)
            );
            if (area == null) return;
            if (area.title() != null) {
                renderData.setTooltip(Component.literal(area.title()));
            }
            renderData.setCursor(area.cursor());
        }
    }

    @Override
    public void drawImage(CustomImage image, float x, float y, float width, float height, HermesRenderer renderer) {
        image.drawImage(x, y, width, height, renderer);
    }

    @Override
    public void onMouseClickedInternal(MouseButton button, float mouseX, float mouseY) {
        super.onMouseClickedInternal(button, mouseX, mouseY);
        if (button != MouseButton.LEFT || map == null) return;
        ImageMap imageMap = this.style.globalData().get(GlobalData.IMAGE_MAP, map);
        if (imageMap == null) return;
        if (!position.isInside(mouseX, mouseY)) return;
        MapArea area = imageMap.getArea(
                (mouseX - position.getX()) * (imageWidth / width),
                (mouseY - position.getY()) * (imageHeight / height)
        );
        if (area == null) return;
        if (area.href() == null) return;
        this.style.getLinkStyle().getBrowserProvider().browse(area.href());
    }

    @Override
    public void close() {
        super.close();
        if (this.image == null) return;
        this.image.close();
    }
}
