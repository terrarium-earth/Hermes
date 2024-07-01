package earth.terrarium.hermes.elements.html;

import com.teamresourceful.resourcefullib.client.screens.CursorScreen;
import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.elements.impl.ImageElement;
import dev.dediamondpro.minemark.utils.MouseButton;
import earth.terrarium.hermes.data.GlobalData;
import earth.terrarium.hermes.data.map.ImageMap;
import earth.terrarium.hermes.data.map.MapArea;
import earth.terrarium.hermes.image.CustomImage;
import earth.terrarium.hermes.renderer.HermesRenderer;
import earth.terrarium.hermes.styles.HermesStyle;
import net.minecraft.Optionull;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

public class HtmlImage extends ImageElement<HermesStyle, HermesRenderer, CustomImage> {

    private final String map;

    public HtmlImage(@NotNull HermesStyle style, @NotNull LayoutStyle layoutStyle, @Nullable Element<HermesStyle, HermesRenderer> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);
        assert attributes != null;

        this.map = Optionull.map(
                attributes.getValue("usemap"),
                s -> s.startsWith("#") ? s.substring(1) : null
        );
    }

    @Override
    public void drawInternal(float xOffset, float yOffset, float mouseX, float mouseY, HermesRenderer renderData) {
        super.drawInternal(xOffset, yOffset, mouseX, mouseY, renderData);
        if (this.image == null) return;
        CursorScreen.Cursor cursor = this.layoutStyle.get(AttributesGlobal.CURSOR);
        if (cursor != null) {
            renderData.setCursor(cursor);
        } else {
            ImageMap imageMap = this.style.getGlobalData().get(GlobalData.IMAGE_MAP, map);
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
    @SuppressWarnings("UnstableApiUsage")
    public void onMouseClickedInternal(MouseButton button, float mouseX, float mouseY) {
        super.onMouseClickedInternal(button, mouseX, mouseY);
        if (button != MouseButton.LEFT || map == null) return;
        ImageMap imageMap = this.style.getGlobalData().get(GlobalData.IMAGE_MAP, map);
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
        this.image.close();
    }
}
