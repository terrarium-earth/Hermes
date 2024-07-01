package earth.terrarium.hermes.elements.base;

import dev.dediamondpro.minemark.LayoutData;
import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.elements.BasicElement;
import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.style.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

public class NoOpElement<S extends Style, R> extends BasicElement<S, R> {

    public NoOpElement(@NotNull S style, @NotNull LayoutStyle layoutStyle, @Nullable Element<S, R> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);
    }

    @Override
    protected void drawElement(float x, float y, float width, float height, R renderData) {

    }

    @Override
    protected float getWidth(LayoutData layoutData, R renderData) {
        return 0;
    }

    @Override
    protected float getHeight(LayoutData layoutData, R renderData) {
        return 0;
    }
}
