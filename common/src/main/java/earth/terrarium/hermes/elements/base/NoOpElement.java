package earth.terrarium.hermes.elements.base;

import dev.dediamondpro.minemark.LayoutData;
import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.elements.BasicElement;
import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.elements.creators.ElementCreator;
import dev.dediamondpro.minemark.style.Style;
import earth.terrarium.hermes.elements.html.Html;
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

    public static class Creator<S extends Style, R> implements ElementCreator<S, R> {

        @Override
        public Element<S, R> createElement(S style, LayoutStyle layoutStyle, @NotNull Element<S, R> parent, @NotNull String qName, @NotNull Attributes attributes) {
            return new NoOpElement<>(style, layoutStyle, parent, qName, attributes);
        }

        @Override
        public boolean appliesTo(S style, LayoutStyle layoutStyle, @NotNull Element<S, R> parent, @NotNull String qName, @NotNull Attributes attributes) {
            return Html.NO_DISPLAY.contains(qName);
        }
    }
}
