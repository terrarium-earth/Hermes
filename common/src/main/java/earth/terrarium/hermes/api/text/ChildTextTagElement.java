package earth.terrarium.hermes.api.text;

import earth.terrarium.hermes.api.TagElement;
import net.minecraft.network.chat.MutableComponent;

public interface ChildTextTagElement extends TagElement {

    MutableComponent component();

    @Override
    default void addChild(TagElement element) {
        if (element instanceof ChildTextTagElement textTag) {
            component().append(textTag.component());
        } else {
            throw new UnsupportedOperationException();
        }
    }
}

