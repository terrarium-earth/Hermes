package earth.terrarium.hermes.api;

public interface Alignable {

    enum Alignment {
        LEFT,
        CENTER,
        RIGHT
    }

    enum vAlignment {
        TOP,
        MIDDLE,
        BOTTOM
    }

    default int getOffset(int areaWidth, int elementWidth, Alignment align) {
        // Returns an offset to align 'elementWidth' _within_ 'areaWidth'
        return switch (align) {
            case LEFT -> 0;
            case RIGHT -> (areaWidth - elementWidth);
            case CENTER -> Math.round((areaWidth - elementWidth) / 2f);
        };
   }

    default int getOffset(float areaWidth, float elementWidth, Alignment align) {
        // Returns an offset to align 'elementWidth' _within_ 'areaWidth'
        return switch (align) {
            case LEFT -> 0;
            case RIGHT -> Math.round(areaWidth - elementWidth);
            case CENTER -> Math.round((areaWidth - elementWidth) / 2f);
        };
    }

    default int getOffsetV(float areaHeight, float elementHeight, vAlignment vAlign) {
        return switch (vAlign) {
            case TOP -> 0;
            case BOTTOM -> Math.round(areaHeight - elementHeight);
            case MIDDLE -> Math.round((areaHeight - elementHeight) / 2f);
        };
    }
}
