package earth.terrarium.hermes.elements.html.map;

import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.elements.Element;
import earth.terrarium.hermes.data.map.ImageMap;
import earth.terrarium.hermes.data.map.MapArea;
import earth.terrarium.hermes.elements.base.NoOpElement;
import earth.terrarium.hermes.renderer.HermesRenderer;
import earth.terrarium.hermes.styles.HermesStyle;
import earth.terrarium.hermes.utils.AttributeParser;
import earth.terrarium.hermes.utils.CssParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class HtmlArea extends NoOpElement<HermesStyle, HermesRenderer> {

    public HtmlArea(@NotNull HermesStyle style, @NotNull LayoutStyle layoutStyle, @Nullable Element<HermesStyle, HermesRenderer> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);
        assert attributes != null;
        if (!(parent instanceof HtmlMap map)) return;
        ImageMap imageMap = map.getMap();
        if (imageMap == null) return;
        Shape shape = parse(attributes.getValue("shape"), attributes.getValue("coords"));
        if (shape == null) return;
        String title = AttributeParser.parseString(attributes, "title", null);
        String href = AttributeParser.parseString(attributes, "href", "");
        var css = CssParser.parseInlineCss(attributes.getValue("style"));
        var cursor = CssParser.parseCursor(css.get("cursor"), "a");
        imageMap.addArea(new MapArea(title, href, cursor, shape));
    }

    private static Shape parse(String shape, String coords) {
        if (shape == null || coords == null) return null;
        String[] split = coords.split(",");
        int[] points = new int[split.length];
        for (int i = 0; i < split.length; i++) {
            points[i] = Integer.parseInt(split[i]);
        }
        return switch (shape) {
            case "rect" -> {
                if (points.length != 4) yield null;
                yield new Rectangle(points[0], points[1], points[2] - points[0], points[3] - points[1]);
            }
            case "circle" -> {
                if (points.length != 3) yield null;
                yield new Ellipse2D.Float(points[0] - points[2], points[1] - points[2], points[2] * 2, points[2] * 2);
            }
            case "poly" -> {
                if (points.length % 2 != 0) yield null;
                Polygon polygon = new Polygon();
                for (int i = 0; i < points.length; i += 2) {
                    polygon.addPoint(points[i], points[i + 1]);
                }
                if (points[0] != points[points.length - 2] || points[1] != points[points.length - 1]) {
                    polygon.addPoint(points[0], points[1]);
                }
                yield polygon;
            }
            default -> null;
        };
    }
}
