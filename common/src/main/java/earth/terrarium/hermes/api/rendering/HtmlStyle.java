package earth.terrarium.hermes.api.rendering;

import dev.dediamondpro.minemark.style.Style;
import earth.terrarium.hermes.api.data.HtmlData;

public interface HtmlStyle extends Style {

    HtmlData data();

    @Override
    HtmlBlockquoteStyleConfig getBlockquoteStyle();

    void onStart();
}
