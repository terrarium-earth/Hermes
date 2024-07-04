package earth.terrarium.hermessvg;

import dev.dediamondpro.minemark.LayoutData;
import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.elements.BasicElement;
import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.providers.ImageProvider;
import earth.terrarium.hermes.api.image.CustomImage;
import earth.terrarium.hermes.api.rendering.HtmlRenderer;
import earth.terrarium.hermes.api.rendering.HtmlStyle;
import earth.terrarium.hermes.utils.AttributeParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

import java.util.regex.Pattern;

public class SvgElement extends BasicElement<HtmlStyle, HtmlRenderer> {

    private static final Pattern pixelPattern = Pattern.compile("^\\d+(px)?$");

    private float width = 0;
    private float height = 0;
    private float imageWidth = 0;
    private float imageHeight = 0;

    private boolean failed = false;
    private CustomImage image;

    public SvgElement(@NotNull HtmlStyle style, @NotNull LayoutStyle layoutStyle, @Nullable Element<HtmlStyle, HtmlRenderer> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);
    }

    @Override
    public void complete() {
        try {
            StringBuilder builder = new StringBuilder();
            builder.append("<svg");
            builder.append(" xmlns=\"http://www.w3.org/2000/svg\"");
            if (attributes != null) {
                for (int i = 0; i < attributes.getLength(); i++) {
                    builder.append(" ");
                    builder.append(attributes.getQName(i));
                    builder.append("=\"");
                    builder.append(attributes.getValue(i));
                    builder.append("\"");
                }
            }
            builder.append(">");
            for (Element<HtmlStyle, HtmlRenderer> child : children) {
                if (!(child instanceof SvgEntryElement element)) {
                    throw new IllegalStateException("Svg can only contain SvgElement children");
                }
                builder.append(element.toElementString());
            }
            builder.append("</svg>");
            String svg = builder.toString();

            assert attributes != null;

            int width = AttributeParser.parseInt(attributes, "width", -1);
            int height = AttributeParser.parseInt(attributes, "height", -1);
            SvgDecoder.decode(width, height, svg, this::onDimensionsReceived, this::onImageReceived);
        } catch (Exception e) {
            failed = true;
        }
    }

    protected void onDimensionsReceived(ImageProvider.Dimension dimension) {
        this.imageWidth = dimension.getWidth();
        this.imageHeight = dimension.getHeight();
        this.regenerateLayout();
    }

    protected void onImageReceived(CustomImage image) {
        this.image = image;
    }

    @Override
    public void generateLayout(LayoutData layoutData, HtmlRenderer renderer) {
        this.calculateDimensions(layoutData);
        super.generateLayout(layoutData, renderer);
    }

    @Override
    protected void drawElement(float x, float y, float width, float height, HtmlRenderer renderer) {
        if (failed) return;
        if (image == null) return;
        image.drawImage(x, y, width, height, renderer);
    }

    @Override
    protected float getWidth(LayoutData layoutData, HtmlRenderer renderer) {
        return this.width;
    }

    @Override
    protected float getHeight(LayoutData layoutData, HtmlRenderer renderer) {
        return this.height;
    }

    protected void calculateDimensions(LayoutData layoutData) {
        if (!(this.width > 0.0F) || !(this.height > 0.0F)) {
            this.width = -1.0F;
            this.height = -1.0F;
            String desiredWidth = this.attributes.getValue("width");
            String desiredHeight = this.attributes.getValue("height");
            if (desiredHeight != null && pixelPattern.matcher(desiredHeight).matches()) {
                this.height = Float.parseFloat(desiredHeight.replaceAll("[^0-9]", ""));
            }

            if (desiredWidth != null) {
                if (pixelPattern.matcher(desiredWidth).matches()) {
                    this.width = Float.parseFloat(desiredWidth.replaceAll("[^0-9]", ""));
                } else if (desiredWidth.endsWith("%")) {
                    this.width = Float.parseFloat(desiredWidth.replaceAll("[^0-9]", "")) / 100.0F * layoutData.getMaxWidth();
                }

                if (this.width > layoutData.getMaxWidth()) {
                    this.width = layoutData.getMaxWidth();
                    this.height = -1.0F;
                }
            }

            if (this.width == -1.0F || this.height == -1.0F) {
                if (this.imageWidth != -1.0F && this.imageHeight != -1.0F) {
                    if (this.width == -1.0F && this.height == -1.0F) {
                        this.width = Math.min(this.imageWidth, layoutData.getMaxWidth());
                    }

                    if (this.width == -1.0F) {
                        this.width = this.height * this.imageWidth / this.imageHeight;
                    }

                    if (this.height == -1.0F) {
                        this.height = this.width * this.imageHeight / this.imageWidth;
                    }

                } else {
                    if (this.width == -1.0F) {
                        this.width = 0.0F;
                    }

                    if (this.height == -1.0F) {
                        this.height = 0.0F;
                    }

                }
            }
        }
    }
}
