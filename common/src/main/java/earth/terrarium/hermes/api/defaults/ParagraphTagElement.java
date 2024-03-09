package earth.terrarium.hermes.api.defaults;

import earth.terrarium.hermes.utils.ElementParsingUtils;

import java.util.Map;

public class ParagraphTagElement extends TextTagElement {

    public ParagraphTagElement(Map<String, String> parameters) {
        super(parameters);
        this.shadowed = ElementParsingUtils.parseBoolean(parameters, "shadowed", false);
    }
}
