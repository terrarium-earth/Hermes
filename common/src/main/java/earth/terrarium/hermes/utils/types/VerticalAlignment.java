package earth.terrarium.hermes.utils.types;

public enum VerticalAlignment {
    BASELINE,
    SUB,
    SUPER;

    public float changeOffset(float y, float height) {
        return switch (this) {
            case SUB -> y + height * 0.4f;
            case SUPER -> y - height * 0.1f;
            default -> y;
        };
    }

    public float changeFontSize(float fontSize) {
        return switch (this) {
            case SUB, SUPER -> fontSize * 0.6f;
            default -> fontSize;
        };
    }

    public static VerticalAlignment from(String text, String tag) {
        if (text == null) {
            return switch (tag) {
                case "sub" -> SUB;
                case "sup" -> SUPER;
                default -> BASELINE;
            };
        }
        return switch (text) {
            case "sub" -> SUB;
            case "super" -> SUPER;
            default -> BASELINE;
        };
    }
}
