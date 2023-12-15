package earth.terrarium.hermes.api;

public interface Alignable {

    enum Alignment {
        LEFT,
        CENTER,
        RIGHT
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
}
