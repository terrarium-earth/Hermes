package earth.terrarium.hermes.css;

import java.util.Map;
import java.util.function.Function;

public enum TextDirection {
    LEFT_TO_RIGHT,
    RIGHT_TO_LEFT,
    ;

    public static TextDirection fromCss(Map<String, String> css, Function<String, String> attributes, String tag) {
        return switch (css.get("direction")) {
            case "rtl" -> RIGHT_TO_LEFT;
            case null -> switch (attributes.apply("dir")) {
                case "rtl" -> RIGHT_TO_LEFT;
                case "ltr" -> LEFT_TO_RIGHT;
                case null, default -> "bdo".equals(tag) ? RIGHT_TO_LEFT : LEFT_TO_RIGHT;
            };
            default -> LEFT_TO_RIGHT;
        };
    }
}
