package earth.terrarium.hermes.api.defaults;

import net.minecraft.network.chat.Component;

import java.util.*;

public class ComponentTagElement extends TextTagElement {

    public ComponentTagElement(Map<String, String> parameters) {
        super(parameters);
    }

    public void addText(String content) {
        try {
            this.component = Component.Serializer.fromJson(content);
        } catch (Exception ignored) {
        }
    }

}
