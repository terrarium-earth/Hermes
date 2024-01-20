package earth.terrarium.hermes.api.text;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public record TranslatedTagElement(MutableComponent component) implements ChildTextTagElement {

    public TranslatedTagElement() {
        this(Component.empty());
    }

    @Override
    public void addText(String content) {
        component.append(Component.translatable(content));
    }

}
