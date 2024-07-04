package earth.terrarium.hermes.elements.html.table;

import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.elements.Element;
import earth.terrarium.hermes.api.rendering.HtmlRenderer;
import earth.terrarium.hermes.api.rendering.HtmlStyle;
import earth.terrarium.hermes.elements.html.HtmlDefault;
import earth.terrarium.hermes.utils.CssParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

import java.util.Comparator;

public class HtmlCaption extends HtmlDefault implements Comparable<HtmlCaption> {

    public static final Comparator<Element<?, ?>> COMPARATOR = (a, b) -> {
        if (a instanceof HtmlCaption captionA && b instanceof HtmlCaption captionB) return captionA.compareTo(captionB);
        if (a instanceof HtmlCaption caption) return caption.side == CaptionSide.TOP ? -1 : 1;
        if (b instanceof HtmlCaption caption) return caption.side == CaptionSide.TOP ? 1 : -1;
        return 0;
    };

    private final CaptionSide side;

    public HtmlCaption(@NotNull HtmlStyle style, @NotNull LayoutStyle layoutStyle, @Nullable Element<HtmlStyle, HtmlRenderer> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);
        assert attributes != null;

        this.layoutStyle = this.layoutStyle.clone();

        var css = CssParser.parseInlineCss(attributes.getValue("style"));

        this.side = CaptionSide.of(css.get("caption-side"));
        if (!css.containsKey("text-align")) {
            this.layoutStyle.setAlignment(LayoutStyle.Alignment.CENTER);
        }
    }

    @Override
    public int compareTo(@NotNull HtmlCaption o) {
        if (this.side == o.side) return 0;
        return this.side == CaptionSide.TOP ? -1 : 1;
    }

    private enum CaptionSide {
        TOP, BOTTOM;

        public static CaptionSide of(String name) {
            return "bottom".equals(name) ? BOTTOM : TOP;
        }
    }
}
