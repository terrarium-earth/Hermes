package earth.terrarium.hermes.css;

import java.util.Map;

public enum FontFamily {
    SERIF,
    MONOSPACE,
    ;

    public static FontFamily fromCss(Map<String, String> css, String tag) {
        return switch (css.get("font-family")) {
            case "serif" -> SERIF;
            case "monospace" -> MONOSPACE;
            case null -> switch (tag) {
                case "tt", "code", "kbd", "samp", "pre" -> MONOSPACE;
                default -> SERIF;
            };
            default -> SERIF;
        };
    }
}
