package earth.terrarium.hermes.elements.html;

import com.teamresourceful.resourcefullib.client.CloseablePoseStack;
import dev.dediamondpro.minemark.LayoutData;
import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.elements.impl.TextElement;
import earth.terrarium.hermes.api.rendering.HtmlRenderer;
import earth.terrarium.hermes.api.rendering.HtmlStyle;
import earth.terrarium.hermes.elements.custom.HermesText;
import earth.terrarium.hermes.utils.types.VerticalAlignment;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

import java.awt.*;

public class HtmlParagraph extends TextElement<HtmlStyle, HtmlRenderer> {

    public HtmlParagraph(@NotNull String text, @NotNull HtmlStyle style, @NotNull LayoutStyle layoutStyle, @Nullable Element<HtmlStyle, HtmlRenderer> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(text, style, layoutStyle, parent, qName, attributes);
    }

    @Override
    protected void drawText(@NotNull String text, float x, float y, float fontSize, Color color, boolean hovered, LayoutData.MarkDownElementPosition position, @NotNull HtmlRenderer renderer) {
        text = getPrefix(hovered) + text;
        VerticalAlignment alignment = layoutStyle.get(GlobalAttributesElement.VERTICAL_ALIGNMENT);
        if (alignment != null) {
            y = alignment.changeOffset(y, 8f * fontSize);
            fontSize = alignment.changeFontSize(fontSize);
        }

        float width = renderer.width(text, fontSize);
        if (layoutStyle.get(GlobalAttributesElement.BACKGROUND_COLOR) != null) {
            try (var stack = new CloseablePoseStack(renderer.getGraphics())) {
                stack.scale(fontSize, fontSize, 1f);
                renderer.fill(
                        (x - 1 * fontSize) / fontSize,
                        (y - 1 * fontSize) / fontSize,
                        width / fontSize + 1,
                        9,
                        layoutStyle.get(GlobalAttributesElement.BACKGROUND_COLOR).getRGB()
                );
            }
        }
        renderer.drawString(
                text,
                x,
                y,
                fontSize,
                color.getRGB(),
                false
        );

        if (this.layoutStyle.get(GlobalAttributesElement.TITLE) != null && hovered) {
            renderer.setTooltip(Component.literal(this.layoutStyle.get(GlobalAttributesElement.TITLE)));
        }

        if (this.layoutStyle.get(GlobalAttributesElement.CURSOR) != null && hovered) {
            renderer.setCursor(this.layoutStyle.get(GlobalAttributesElement.CURSOR));
        }
    }

    @Override
    protected void drawInlineCodeBlock(float x, float y, float width, float height, Color color, @NotNull HtmlRenderer renderer) {
        renderer.fill(x, y, width, height, color.getRGB());
    }

    @Override
    protected float getTextWidth(@NotNull String text, float fontSize, HtmlRenderer renderer) {
        VerticalAlignment alignment = layoutStyle.get(GlobalAttributesElement.VERTICAL_ALIGNMENT);
        if (alignment != null) {
            fontSize = alignment.changeFontSize(fontSize);
        }
        return renderer.width(getPrefix(false) + text, fontSize);
    }

    private String getPrefix(boolean hovered) {
        StringBuilder prefixBuilder = new StringBuilder();
        if (layoutStyle.isBold()) prefixBuilder.append("§l");
        if (layoutStyle.isItalic()) prefixBuilder.append("§o");
        if (layoutStyle.isStrikethrough()) prefixBuilder.append("§m");
        if (layoutStyle.isUnderlined() || layoutStyle.isPartOfLink() && hovered) prefixBuilder.append("§n");
        if (layoutStyle.getOrDefault(HermesText.OBFUSCATED, false)) prefixBuilder.append("§k");
        return prefixBuilder.toString();
    }

    @Override
    protected float getBaselineHeight(float fontSize, HtmlRenderer renderData) {
        return 8f * fontSize;
    }
}
