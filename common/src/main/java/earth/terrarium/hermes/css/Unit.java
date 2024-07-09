package earth.terrarium.hermes.css;

public final class Unit {

    public static final Unit ZERO = new Unit(0, false);

    private final boolean percent;
    private final float value;

    public Unit(float value, boolean percent) {
        this.value = value;
        this.percent = percent;
    }

    public Unit(String value) {
        if (value.endsWith("%")) {
            this.value = Float.parseFloat(value.substring(0, value.length() - 1));
            this.percent = true;
        } else if (value.endsWith("px") || value.endsWith("em") || value.endsWith("pt")) {
            float multiplier = value.endsWith("pt") ? 1.3333333333333333f : 1;
            this.value = Float.parseFloat(value.substring(0, value.length() - 2)) * multiplier;
            this.percent = false;
        } else {
            this.value = Float.parseFloat(value);
            this.percent = false;
        }
    }

    public float getValue(float parent) {
        return percent ? value / 100 * parent : value;
    }
}
