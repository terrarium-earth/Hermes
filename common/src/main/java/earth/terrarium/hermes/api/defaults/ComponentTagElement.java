package earth.terrarium.hermes.api.defaults;

import earth.terrarium.hermes.api.TagElement;

import java.util.*;

import static net.minecraft.network.chat.Component.Serializer.fromJson;

@Deprecated
public class ComponentTagElement extends TextTagElement {

    public ComponentTagElement(Map<String, String> parameters) {
        super(parameters);
    }

    @Override
    public void addText(String content) {
        this.component.append(fromJson(content));
    }

    @Override
    public void addChild(TagElement element) {
        throw new UnsupportedOperationException();
    }

}
