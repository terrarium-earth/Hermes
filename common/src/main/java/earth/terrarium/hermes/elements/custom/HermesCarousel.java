package earth.terrarium.hermes.elements.custom;

import com.teamresourceful.resourcefullib.client.screens.CursorScreen;
import dev.dediamondpro.minemark.LayoutData;
import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.utils.MouseButton;
import earth.terrarium.hermes.api.rendering.HtmlRenderer;
import earth.terrarium.hermes.api.rendering.HtmlStyle;
import earth.terrarium.hermes.elements.html.GlobalAttributesElement;
import earth.terrarium.hermes.elements.html.HtmlDefault;
import earth.terrarium.hermes.impl.HermesRenderer;
import earth.terrarium.hermes.utils.AttributeParser;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

public class HermesCarousel extends HtmlDefault {

    private static final int BUTTON_WIDTH = 20;

    private final float height;

    private int currentIndex;
    private float currentWidth = 0f;
    private float currentHeight = 0f;

    public HermesCarousel(@NotNull HtmlStyle style, @NotNull LayoutStyle layoutStyle, @Nullable Element<HtmlStyle, HtmlRenderer> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);
        assert attributes != null;

        this.currentIndex = AttributeParser.parseInt(attributes, "index", 0);
        this.height = AttributeParser.parseFloat(attributes, "height", 0);
    }

    @Override
    public void generateLayout(LayoutData layoutData, HtmlRenderer renderer) {
        if (layoutData.isLineOccupied()) {
            layoutData.nextLine();
        }

        float outsidePadding = getOutsidePadding(layoutData, renderer);
        float insidePadding = getInsidePadding(layoutData, renderer);

        layoutData.updatePadding(outsidePadding);

        LayoutData newLayoutData = new LayoutData(layoutData.getMaxWidth() - BUTTON_WIDTH * 2);
        this.children.get(this.currentIndex).generateLayoutInternal(newLayoutData, renderer);

        this.currentWidth = newLayoutData.getMaxWidth();
        this.currentHeight = Math.max(newLayoutData.getY() + newLayoutData.getCurrentLine().getHeight(), this.height);

        layoutData.addElement(LayoutStyle.Alignment.LEFT, this.currentWidth, this.currentHeight);
        layoutData.nextLine();

        this.extraXOffset = BUTTON_WIDTH + insidePadding;
        this.extraYOffset = insidePadding;
    }

    @Override
    public void drawInternal(float xOffset, float yOffset, float mouseX, float mouseY, HtmlRenderer renderer) {
        if (this.positions.isAnyInside(mouseX, mouseY) ) {
            if (this.layoutStyle.get(GlobalAttributesElement.TITLE) != null) {
                renderer.setTooltip(Component.literal(this.layoutStyle.get(GlobalAttributesElement.TITLE)));
            }
            if (this.layoutStyle.get(GlobalAttributesElement.CURSOR) != null) {
                renderer.setCursor(this.layoutStyle.get(GlobalAttributesElement.CURSOR));
            }
        }

        HermesRenderer.drawDefault(
                xOffset, yOffset,
                currentWidth + BUTTON_WIDTH * 2, this.currentHeight,
                this.layoutStyle, renderer
        );
        this.children.get(this.currentIndex).drawInternal(
                xOffset + extraXOffset, yOffset + extraYOffset,
                mouseX - extraXOffset, mouseY - extraYOffset,
                renderer
        );

        boolean hovered = mouseX > xOffset && mouseX < xOffset + this.currentWidth + BUTTON_WIDTH * 2 && mouseY > yOffset && mouseY < yOffset + this.currentHeight;
        boolean leftHovered = mouseX > xOffset && mouseX < xOffset + BUTTON_WIDTH && hovered;
        boolean rightHovered = mouseX > xOffset + this.currentWidth + BUTTON_WIDTH && hovered;

        renderer.drawString(
                "<",
                xOffset + BUTTON_WIDTH * 0.3f, yOffset + this.currentHeight / 2f - 7f,
                2f,
                leftHovered ? this.layoutStyle.getTextColor().darker().getRGB() : this.layoutStyle.getTextColor().getRGB(),
                false
        );

        renderer.drawString(
                ">",
                xOffset + this.currentWidth + BUTTON_WIDTH + BUTTON_WIDTH * 0.3f, yOffset + this.currentHeight / 2f - 7f,
                2f,
                rightHovered ? this.layoutStyle.getTextColor().darker().getRGB() : this.layoutStyle.getTextColor().getRGB(),
                false
        );

        if (leftHovered || rightHovered) {
            renderer.setCursor(CursorScreen.Cursor.POINTER);
        }
    }

    @Override
    public void beforeDrawInternal(float xOffset, float yOffset, float mouseX, float mouseY, HtmlRenderer renderData) {
        this.children.get(this.currentIndex).beforeDrawInternal(
                xOffset + extraXOffset, yOffset + extraYOffset,
                mouseX - extraXOffset, mouseY - extraYOffset,
                renderData
        );
    }

    @Override
    public void onMouseClickedInternal(MouseButton button, float mouseX, float mouseY) {
        if (mouseY > 0 && mouseY < this.currentHeight) {
            if (mouseX < extraXOffset) {
                this.move(-1);
            } else if (mouseX > this.currentWidth - extraXOffset) {
                this.move(1);
            }
        }
        this.children.get(this.currentIndex).onMouseClickedInternal(button, mouseX - extraXOffset, mouseY - extraYOffset);
    }

    public void move(int amount) {
        this.currentIndex += amount;
        if (this.currentIndex < 0) {
            this.currentIndex = this.children.size() - (Math.abs(this.currentIndex) % this.children.size());
        } else if (this.currentIndex >= this.children.size()) {
            this.currentIndex = this.currentIndex % this.children.size();
        }
        this.regenerateLayout();
    }

    @Override
    protected MarkerType getMarkerType() {
        return MarkerType.FULL;
    }

}
