package earth.terrarium.hermes.api.text;

import earth.terrarium.hermes.api.TagElement;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

import java.util.function.UnaryOperator;

public class DefaultStyledTagElement implements ChildTextTagElement {

    private final MutableComponent component;
    private final MutableComponent defaultComponent;
    private boolean useDefault = true;

    public DefaultStyledTagElement(UnaryOperator<Style> styler, Component component) {
        this.component = Component.empty().withStyle(styler);
        this.defaultComponent = component.copy().withStyle(styler);
    }

    @Override
    public void addText(String content) {
        this.useDefault = false;
        component.append(content);
    }

    @Override
    public void addChild(TagElement element) {
        this.useDefault = false;
        ChildTextTagElement.super.addChild(element);
    }

    @Override
    public MutableComponent component() {
        return this.useDefault ? this.defaultComponent : this.component;
    }
}
