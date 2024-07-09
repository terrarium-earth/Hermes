package earth.terrarium.hermes.elements.html;

import dev.dediamondpro.minemark.LayoutData;
import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.elements.impl.TextElement;
import earth.terrarium.hermes.api.rendering.HtmlRenderer;
import earth.terrarium.hermes.api.rendering.HtmlStyle;
import earth.terrarium.hermes.css.FontFamily;
import earth.terrarium.hermes.css.TextDirection;
import earth.terrarium.hermes.css.VerticalAlignment;
import earth.terrarium.hermes.elements.custom.HermesText;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

import java.awt.*;

public class HtmlParagraph extends TextElement<HtmlStyle, HtmlRenderer> {

    public HtmlParagraph(@NotNull String text, @NotNull HtmlStyle style, @NotNull LayoutStyle layoutStyle, @Nullable Element<HtmlStyle, HtmlRenderer> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(format(text, layoutStyle), style, layoutStyle, parent, qName, attributes);
    }

    private static String format(String text, LayoutStyle style) {
        if (style.getOrDefault(GlobalAttributesElement.DIRECTION, TextDirection.LEFT_TO_RIGHT) == TextDirection.RIGHT_TO_LEFT) {
            return new StringBuilder(text).reverse().toString();
        }
        return text;
    }

    @Override
    protected void drawText(@NotNull String text, float x, float y, float fontSize, Color color, boolean hovered, LayoutData.MarkDownElementPosition position, @NotNull HtmlRenderer renderer) {
        text = getPrefix(hovered) + text;
        VerticalAlignment alignment = layoutStyle.get(GlobalAttributesElement.VERTICAL_ALIGNMENT);
        if (alignment != null) {
            y = alignment.changeOffset(y, 8f * fontSize);
            fontSize = alignment.changeFontSize(fontSize);
        }
        renderer.drawString(
                text,
                x,
                y + 1,
                fontSize,
                color.getRGB(),
                false,
                layoutStyle.get(GlobalAttributesElement.FONT_FAMILY) == FontFamily.MONOSPACE
        );
    }

    @Override
    protected void drawInlineCodeBlock(float x, float y, float width, float height, Color color, @NotNull HtmlRenderer renderer) {
        renderer.fill(x, y, width, height, color.getRGB());
    }

    @Override
    protected float getTextWidth(@NotNull String text, float fontSize, HtmlRenderer renderer) {
        text = getPrefix(false) + text;
        VerticalAlignment alignment = layoutStyle.get(GlobalAttributesElement.VERTICAL_ALIGNMENT);
        if (alignment != null) {
            return renderer.width(text, alignment.changeFontSize(fontSize), layoutStyle.isPartOfCodeBlock()) + 1f;
        }
        return renderer.width(text, fontSize, layoutStyle.isPartOfCodeBlock());
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
        return 9f * fontSize;
    }
}
