package earth.terrarium.hermes.utils;

import dev.dediamondpro.minemark.LayoutData;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class MarkdownPositions {

    private float left = -1;
    private float right = -1;
    private float top = -1;
    private float bottom = -1;

    public <R> void init(LayoutData data, R renderer, BiConsumer<LayoutData, R> updater) {
        List<LayoutData.MarkDownElementPosition> positions = new ArrayList<>();
        data.addElementListener(positions::add);
        updater.accept(data, renderer);
        data.removeElementListener();
        this.left = this.top = this.right = this.bottom = -1;

        for (LayoutData.MarkDownElementPosition position : positions) {
            float posLeft = position.getX();
            float posTop = position.getY();
            float posRight = position.getRightX();
            float posBottom = position.getBottomY();
            if (this.left == -1 || posLeft < this.left) this.left = posLeft;
            if (this.top == -1 || posTop < this.top) this.top = posTop;
            if (this.right == -1 || posRight > this.right) this.right = posRight;
            if (this.bottom == -1 || posBottom > this.bottom) this.bottom = posBottom;
        }
    }

    public boolean isAnyInside(float x, float y) {
        return x >= left && x <= right && y >= top && y <= bottom;
    }

    public float x() {
        return left;
    }

    public float y() {
        return top;
    }

    public float width() {
        return right - left;
    }

    public float height() {
        return bottom - top;
    }

    public boolean hasValues() {
        return left != -1 && right != -1 && top != -1 && bottom != -1;
    }
}
