package earth.terrarium.hermes.css;

import dev.dediamondpro.minemark.utils.ColorFactory;
import org.joml.Vector4f;

import java.awt.*;
import java.util.Map;

public class Border {

    public static final Border NONE = new Border(null, null, null);

    private final Unit[] radius;
    private final Color color;
    private final Unit width;

    private Border(Unit[] radius, Color color, Unit width) {
        this.radius = radius;
        this.color = color;
        this.width = width;
    }

    public static Border fromCss(Map<String, String> css) {
        Unit[] radius = parseBorderRadius(css);
        Color color = parseBorderColor(css);
        Unit width = parseBorderWidth(css);
        return new Border(radius, color, width);
    }

    private static Unit[] parseBorderRadius(Map<String, String> css) {
        // top-right, bottom-right, top-left, bottom-left
        Unit[] radius = new Unit[4];
        String value = css.get("border-radius");
        if (value != null) {
            String[] splits = value.split(" ");
            if (splits.length == 1) {
                radius[0] = radius[1] = radius[2] = radius[3] = new Unit(splits[0]);
            } else if (splits.length == 2) {
                radius[2] = radius[1] = new Unit(splits[0]);
                radius[0] = radius[3] = new Unit(splits[1]);
            } else if (splits.length == 3) {
                radius[2] = new Unit(splits[0]);
                radius[0] = radius[1] = new Unit(splits[1]);
                radius[3] = new Unit(splits[2]);
            } else {
                radius[2] = new Unit(splits[0]);
                radius[0] = new Unit(splits[1]);
                radius[1] = new Unit(splits[2]);
                radius[3] = new Unit(splits[3]);
            }
        }
        String left = css.get("border-top-left-radius");
        if (left != null) radius[0] = new Unit(left);
        String right = css.get("border-top-right-radius");
        if (right != null) radius[1] = new Unit(right);
        String bottomRight = css.get("border-bottom-right-radius");
        if (bottomRight != null) radius[2] = new Unit(bottomRight);
        String bottomLeft = css.get("border-bottom-left-radius");
        if (bottomLeft != null) radius[3] = new Unit(bottomLeft);
        if (radius[0] == null && radius[1] == null && radius[2] == null && radius[3] == null) return null;
        if (radius[0] == null) radius[0] = Unit.ZERO;
        if (radius[1] == null) radius[1] = Unit.ZERO;
        if (radius[2] == null) radius[2] = Unit.ZERO;
        if (radius[3] == null) radius[3] = Unit.ZERO;
        return radius;
    }

    private static Color parseBorderColor(Map<String, String> css) {
        String color = css.get("border-color");
        if (color != null) {
            return ColorFactory.web(color);
        }
        return null;
    }

    private static Unit parseBorderWidth(Map<String, String> css) {
        String width = css.get("border-width");
        if (width != null) {
            return new Unit(width);
        }
        return null;
    }

    public Vector4f getRadius(float width, float height) {
        if (radius == null) return null;
        float min = Math.min(width, height);
        return new Vector4f(radius[0].getValue(min), radius[1].getValue(min), radius[2].getValue(min), radius[3].getValue(min));
    }

    public int getColor(Color fallback) {
        return color != null ? color.getRGB() : fallback.getRGB();
    }

    public int getColor() {
        return color != null ? color.getRGB() : 0;
    }

    public float getWidth(float width, float height) {
        float parent = Math.min(width, height);
        return this.width != null ? this.width.getValue(parent) : 0;
    }
}
