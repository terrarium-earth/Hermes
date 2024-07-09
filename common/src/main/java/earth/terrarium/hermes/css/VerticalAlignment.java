package earth.terrarium.hermes.css;

import java.util.Map;

public enum VerticalAlignment {
    BASELINE,
    SUB,
    SUPER;

    public float changeOffset(float y, float height) {
        return switch (this) {
            case SUB -> y + height * 0.5f;
            case SUPER -> y - height * 0.3f;
            default -> y;
        };
    }

    public float changeFontSize(float fontSize) {
        return switch (this) {
            case SUB, SUPER -> fontSize * 0.6f;
            default -> fontSize;
        };
    }

    public static VerticalAlignment fromCss(Map<String, String> css, String tag) {
        return switch (css.get("vertical-align")) {
            case "sub" -> SUB;
            case "super" -> SUPER;
            case "baseline" -> BASELINE;
            case null -> switch (tag) {
                case "sub" -> SUB;
                case "sup" -> SUPER;
                default -> null;
            };
            default -> BASELINE;
        };
    }
}
