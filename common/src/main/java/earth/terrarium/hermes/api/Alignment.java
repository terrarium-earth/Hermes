package earth.terrarium.hermes.api;

//public interface Alignable {

 public enum Alignment {
        MIN,
        MIDDLE,
        MAX;

    public static Alignment fromString(String name) {
        return switch (name) {
            case "LEFT", "TOP" -> Alignment.MIN;
            case "RIGHT", "BOTTOM" -> Alignment.MAX;
            case "CENTER", "MIDDLE" -> Alignment.MIDDLE;
            default -> throw new IllegalStateException("Unexpected value: " + name);
        };
    }

    public static int getOffset(float areaSize, float elementSize, Alignment align) {
        // Returns an offset to align 'elementWidth' _within_ 'areaWidth'
        return switch (align) {
            case MIN -> 0;
            case MAX -> Math.round(areaSize - elementSize);
            case MIDDLE -> Math.round((areaSize - elementSize) / 2f);
        };
    }

}
