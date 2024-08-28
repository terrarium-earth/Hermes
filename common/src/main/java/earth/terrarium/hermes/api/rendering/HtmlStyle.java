package earth.terrarium.hermes.api.rendering;

import dev.dediamondpro.minemark.style.Style;
import earth.terrarium.hermes.api.data.HtmlData;
import earth.terrarium.hermes.api.links.LinkHandler;

public interface HtmlStyle extends Style {

    HtmlData data();

    @Override
    HtmlBlockquoteStyleConfig getBlockquoteStyle();

    void onStart();

    void addLinkHandler(LinkHandler handler);
}
