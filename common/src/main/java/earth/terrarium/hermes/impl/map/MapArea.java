package earth.terrarium.hermes.impl.map;

import com.teamresourceful.resourcefullib.client.screens.CursorScreen;

import java.awt.*;

public class MapArea {

    private final String title;
    private final String href;
    private final CursorScreen.Cursor cursor;
    private final Shape shape;

    public MapArea(String title, String href, CursorScreen.Cursor cursor, Shape shape) {
        this.title = title;
        this.href = href;
        this.cursor = cursor;
        this.shape = shape;
    }

    public String title() {
        return title;
    }

    public String href() {
        return href;
    }

    public CursorScreen.Cursor cursor() {
        return cursor;
    }

    public boolean isInside(int x, int y) {
        return shape.contains(x, y);
    }
}
