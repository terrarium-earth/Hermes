package earth.terrarium.hermes.compat.mcef;

import com.teamresourceful.resourcefullib.common.utils.modinfo.ModInfoUtils;
import dev.dediamondpro.minemark.MineMarkCoreBuilder;
import earth.terrarium.hermes.api.ElementExtension;
import earth.terrarium.hermes.api.rendering.HtmlRenderer;
import earth.terrarium.hermes.api.rendering.HtmlStyle;

public class McefExtension implements ElementExtension {

    private final boolean enabled;

    public McefExtension() {
        this.enabled = ModInfoUtils.isModLoaded("mcef");
    }

    @Override
    public void addDefaultElements(MineMarkCoreBuilder<HtmlStyle, HtmlRenderer> builder) {
        if (!this.enabled) return;

        builder.addElement("iframe", HtmlIframeElement::new);
    }
}
