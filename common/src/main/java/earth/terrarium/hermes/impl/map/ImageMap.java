package earth.terrarium.hermes.impl.map;

import earth.terrarium.hermes.api.data.HtmlData;

import java.util.ArrayList;
import java.util.List;

public class ImageMap {

    public static final HtmlData.Key<ImageMap> DATA_KEY = new HtmlData.Key<>(ImageMap.class);

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
