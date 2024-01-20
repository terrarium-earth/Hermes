package earth.terrarium.hermes.api.text;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public record KeyTagElement(MutableComponent component) implements ChildTextTagElement {

    public KeyTagElement() {
        this(Component.empty());
    }

    @Override
    public void addText(String content) {
        component.append(Component.keybind(content));
    }

}
