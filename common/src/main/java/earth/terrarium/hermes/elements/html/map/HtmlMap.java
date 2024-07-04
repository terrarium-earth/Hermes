package earth.terrarium.hermes.elements.html.map;

import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.elements.Element;
import earth.terrarium.hermes.api.rendering.HtmlStyle;
import earth.terrarium.hermes.elements.base.NoOpElement;
import earth.terrarium.hermes.impl.map.ImageMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

public class HtmlMap<R> extends NoOpElement<HtmlStyle, R> {

    private final ImageMap map;

    public HtmlMap(@NotNull HtmlStyle style, @NotNull LayoutStyle layoutStyle, @Nullable Element<HtmlStyle, R> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);
        assert attributes != null;
        String name = attributes.getValue("name");
        if (name == null) {
            this.map = null;
        } else {
            this.map = new ImageMap();
            style.data().put(ImageMap.DATA_KEY, name, this.map);
        }
    }

    public ImageMap getMap() {
        return map;
    }
}
