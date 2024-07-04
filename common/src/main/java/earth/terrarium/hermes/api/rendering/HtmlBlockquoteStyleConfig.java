package earth.terrarium.hermes.api.rendering;

import dev.dediamondpro.minemark.style.BlockquoteStyleConfig;

import java.awt.*;

public class HtmlBlockquoteStyleConfig extends BlockquoteStyleConfig {

    private final Color backgroundColor;

    public HtmlBlockquoteStyleConfig(float padding, float spacingLeft, float blockWidth, float spacingRight, Color blockColor, Color backgroundColor) {
        super(padding, spacingLeft, blockWidth, spacingRight, blockColor);
        this.backgroundColor = backgroundColor;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }
}
