package earth.terrarium.hermes.utils;

import com.teamresourceful.resourcefullib.client.screens.CursorScreen;
import dev.dediamondpro.minemark.utils.ColorFactory;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class CssParser {

    public static Map<String, String> parseInlineCss(String css) {
        if (css == null) return Map.of();
        css = css.trim();
        String[] splits = css.split(";");
        Map<String, String> map = new HashMap<>();
        for (String split : splits) {
            String[] keyValue = split.split(":");
            if (keyValue.length == 2) {
                map.put(keyValue[0].trim(), keyValue[1].trim());
            }
        }
        return map;
    }

    public static Color parseBackgroundColor(String css, String element) {
        if (css == null) {
            return switch (element) {
                case "mark" -> ColorFactory.YELLOW;
                default -> null;
            };
        }
        try {
            return ColorFactory.web(css);
        } catch (Exception e) {
            return ColorFactory.BLACK;
        }
    }

    public static CursorScreen.Cursor parseCursor(String css, String element) {
        if (css == null) {
            return switch (element) {
                case "a" -> CursorScreen.Cursor.POINTER;
                default -> null;
            };
        }
        return switch (css) {
            case "all-scroll", "col-resize", "move", "row-resize" -> CursorScreen.Cursor.RESIZE_ALL;
            case "e-resize", "ew-resize", "w-resize" -> CursorScreen.Cursor.RESIZE_EW;
            case "n-resize", "ns-resize", "s-resize" -> CursorScreen.Cursor.RESIZE_NS;
            case "nesw-resize", "ne-resize", "sw-resize" -> CursorScreen.Cursor.RESIZE_NESW;
            case "nwse-resize", "nw-resize", "se-resize" -> CursorScreen.Cursor.RESIZE_NWSE;

            case "crosshair" -> CursorScreen.Cursor.CROSSHAIR;
            case "grab", "grabbing", "pointer" -> CursorScreen.Cursor.POINTER;
            case "no-drop", "not-allowed" -> CursorScreen.Cursor.DISABLED;
            case "text", "vertical-text" -> CursorScreen.Cursor.TEXT;

            default -> CursorScreen.Cursor.DEFAULT;
        };
    }
}
