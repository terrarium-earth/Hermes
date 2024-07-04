package earth.terrarium.hermes.api;

import com.google.common.base.Suppliers;
import dev.dediamondpro.minemark.MineMarkCoreBuilder;
import earth.terrarium.hermes.api.rendering.HtmlRenderer;
import earth.terrarium.hermes.api.rendering.HtmlStyle;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.function.Supplier;

public interface ElementExtension {

    Supplier<List<ElementExtension>> EXTENSIONS = Suppliers.memoize(() -> {
        List<ElementExtension> extensions = new ArrayList<>();
        ServiceLoader.load(ElementExtension.class).forEach(extensions::add);
        return extensions;
    });

    default void addDefaultElements(MineMarkCoreBuilder<HtmlStyle, HtmlRenderer> builder) {

    }

    default void addDefaultFormatting(MineMarkCoreBuilder<HtmlStyle, HtmlRenderer> builder) {

    }
}
