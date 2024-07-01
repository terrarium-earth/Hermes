package earth.terrarium.hermes.styles;

import dev.dediamondpro.minemark.style.BlockquoteStyleConfig;

import java.awt.*;

public class HermesBlockquoteStyle extends BlockquoteStyleConfig {

    private final Color backgroundColor;

    public HermesBlockquoteStyle(float padding, float spacingLeft, float blockWidth, float spacingRight, Color blockColor, Color backgroundColor) {
        super(padding, spacingLeft, blockWidth, spacingRight, blockColor);
        this.backgroundColor = backgroundColor;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }
}
