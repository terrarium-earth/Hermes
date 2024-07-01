package earth.terrarium.hermes.utils;

import dev.dediamondpro.minemark.LayoutData;

import java.util.ArrayList;
import java.util.function.BiConsumer;

public class MarkdownPositions {

    protected final ArrayList<LayoutData.MarkDownElementPosition> positions = new ArrayList<>();

    public <R> void init(LayoutData data, R renderer, BiConsumer<LayoutData, R> updater) {
        this.positions.clear();
        data.addElementListener(this.positions::add);
        updater.accept(data, renderer);
        data.removeElementListener();
    }

    public boolean isAnyInside(float x, float y) {
        for (LayoutData.MarkDownElementPosition position : positions) {
            if (position.isInside(x, y)) {
                return true;
            }
        }
        return false;
    }
}
