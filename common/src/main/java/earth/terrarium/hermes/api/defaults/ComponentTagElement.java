package earth.terrarium.hermes.api.defaults;

import java.util.*;

import static net.minecraft.network.chat.Component.Serializer.fromJson;

public class ComponentTagElement extends TextTagElement {

    public ComponentTagElement(Map<String, String> parameters) {
        super(parameters);
    }

    @Override
    public void addText(String content) {
        this.component.append(fromJson(content));
    }
}
