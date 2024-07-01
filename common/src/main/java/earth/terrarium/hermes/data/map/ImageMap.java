package earth.terrarium.hermes.data.map;

import java.util.ArrayList;
import java.util.List;

public class ImageMap {

    private final List<MapArea> areas = new ArrayList<>();

    public void addArea(MapArea area) {
        areas.add(area);
    }

    public MapArea getArea(float x, float y) {
        for (MapArea area : areas) {
            if (area.isInside((int) x, (int) y)) {
                return area;
            }
        }
        return null;
    }
}
