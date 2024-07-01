package earth.terrarium.hermes.elements.custom;

import dev.dediamondpro.minemark.LayoutData;
import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.elements.BasicElement;
import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.elements.Inline;
import earth.terrarium.hermes.renderer.HermesRenderer;
import earth.terrarium.hermes.styles.HermesStyle;
import earth.terrarium.hermes.utils.AttributeParser;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

public class HermesItem extends BasicElement<HermesStyle, HermesRenderer> implements Inline {

    private final ItemStack stack;
    private final float scale;

    public HermesItem(@NotNull HermesStyle style, @NotNull LayoutStyle layoutStyle, @Nullable Element<HermesStyle, HermesRenderer> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);
        assert attributes != null;

        this.scale = AttributeParser.parseFloat(attributes, "scale", 1f);
        this.stack = AttributeParser.parseItem(attributes, "item");
    }

    @Override
    protected void drawElement(float x, float y, float width, float height, HermesRenderer renderer) {
        renderer.drawItem(this.stack, x, y, this.scale);
    }

    @Override
    protected float getWidth(LayoutData layoutData, HermesRenderer renderData) {
        return 16f * this.scale;
    }

    @Override
    protected float getHeight(LayoutData layoutData, HermesRenderer renderData) {
        return 16f * this.scale;
    }
}
