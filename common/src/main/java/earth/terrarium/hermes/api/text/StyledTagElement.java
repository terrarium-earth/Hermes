package earth.terrarium.hermes.api.text;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

import java.util.function.UnaryOperator;

public record StyledTagElement(MutableComponent component) implements ChildTextTagElement {

    public StyledTagElement(UnaryOperator<Style> styler) {
        this(Component.empty().withStyle(styler));
    }

    @Override
    public void addText(String content) {
        component.append(content);
    }
}
