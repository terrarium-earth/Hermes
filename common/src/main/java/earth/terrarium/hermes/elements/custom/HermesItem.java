package earth.terrarium.hermes.elements.custom;

import com.teamresourceful.resourcefullib.client.CloseablePoseStack;
import dev.dediamondpro.minemark.LayoutData;
import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.elements.BasicElement;
import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.elements.Inline;
import earth.terrarium.hermes.api.rendering.HtmlRenderer;
import earth.terrarium.hermes.api.rendering.HtmlStyle;
import earth.terrarium.hermes.utils.AttributeParser;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

public class HermesItem extends BasicElement<HtmlStyle, HtmlRenderer> implements Inline {

    private final ItemStack stack;
    private final float scale;

    public HermesItem(@NotNull HtmlStyle style, @NotNull LayoutStyle layoutStyle, @Nullable Element<HtmlStyle, HtmlRenderer> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);
        assert attributes != null;

        this.scale = AttributeParser.parseFloat(attributes, "scale", 1f);
        this.stack = AttributeParser.parseItem(attributes, "item");
    }

    @Override
    protected void drawElement(float x, float y, float width, float height, HtmlRenderer renderer) {
        try (var stack = new CloseablePoseStack(renderer.getGraphics())) {
            stack.scale(this.scale, this.scale, 1f);
            renderer.getGraphics().renderFakeItem(
                    this.stack,
                    (int) (x / this.scale),
                    (int) (y / this.scale)
            );
        }
    }

    @Override
    protected float getWidth(LayoutData layoutData, HtmlRenderer renderData) {
        return 16f * this.scale;
    }

    @Override
    protected float getHeight(LayoutData layoutData, HtmlRenderer renderData) {
        return 16f * this.scale;
    }
}
