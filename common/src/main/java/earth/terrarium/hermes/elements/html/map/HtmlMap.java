package earth.terrarium.hermes.elements.html.map;

import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.elements.Element;
import earth.terrarium.hermes.data.GlobalData;
import earth.terrarium.hermes.data.map.ImageMap;
import earth.terrarium.hermes.elements.base.NoOpElement;
import earth.terrarium.hermes.renderer.HermesRenderer;
import earth.terrarium.hermes.styles.HermesStyle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

public class HtmlMap extends NoOpElement<HermesStyle, HermesRenderer> {

    private final ImageMap map;

    public HtmlMap(@NotNull HermesStyle style, @NotNull LayoutStyle layoutStyle, @Nullable Element<HermesStyle, HermesRenderer> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);
        assert attributes != null;
        String name = attributes.getValue("name");
        if (name == null) {
            this.map = null;
        } else {
            this.map = new ImageMap();
            style.getGlobalData().put(GlobalData.IMAGE_MAP, name, this.map);
        }
    }

    public ImageMap getMap() {
        return map;
    }
}
