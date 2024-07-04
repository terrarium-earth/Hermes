package earth.terrarium.hermessvg;

import dev.dediamondpro.minemark.MineMarkCoreBuilder;
import earth.terrarium.hermes.api.ElementExtension;
import earth.terrarium.hermes.api.rendering.HtmlRenderer;
import earth.terrarium.hermes.api.rendering.HtmlStyle;
import org.apache.batik.css.parser.Parser;
import org.apache.batik.util.XMLResourceDescriptor;

public class SvgExtension implements ElementExtension {

    public SvgExtension() {
        XMLResourceDescriptor.setCSSParserClassName(Parser.class.getName());
    }

    @Override
    public void addDefaultElements(MineMarkCoreBuilder<HtmlStyle, HtmlRenderer> builder) {
        builder.addElement("svg", SvgElement::new);
        builder.addElement(SvgEntryElement.CREATOR);
    }
}
