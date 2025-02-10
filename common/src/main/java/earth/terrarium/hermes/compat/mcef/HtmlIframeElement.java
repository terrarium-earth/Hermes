package earth.terrarium.hermes.compat.mcef;

import com.cinemamod.mcef.MCEF;
import com.cinemamod.mcef.MCEFBrowser;
import com.mojang.blaze3d.platform.InputConstants;
import com.teamresourceful.resourcefullib.client.screens.CursorScreen;
import dev.dediamondpro.minemark.LayoutData;
import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.utils.MouseButton;
import earth.terrarium.hermes.api.rendering.HtmlRenderer;
import earth.terrarium.hermes.api.rendering.HtmlStyle;
import earth.terrarium.hermes.elements.base.BasicBasicElement;
import earth.terrarium.hermes.utils.AttributeParser;
import org.apache.http.util.Asserts;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.Attributes;

import java.net.URI;
import java.util.Set;

public class HtmlIframeElement extends BasicBasicElement<HtmlStyle, HtmlRenderer> {

    private static final Set<String> ALLOWED_HOSTS = Set.of(
            "www.youtube.com",
            "youtube.com",
            "www.youtube-nocookie.com",
            "youtube-nocookie.com"
    );

    private final @Nullable MCEFBrowser browser;

    private int width;
    private int height;

    private int mouseX;
    private int mouseY;

    private int lastX = -1;
    private int lastY = -1;

    private CursorScreen.Cursor cursor = CursorScreen.Cursor.DEFAULT;

    public HtmlIframeElement(@NotNull HtmlStyle style, @NotNull LayoutStyle layoutStyle, @Nullable Element<HtmlStyle, HtmlRenderer> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);
        assert attributes != null;

        this.width = AttributeParser.parseInt(attributes, "width", 300);
        this.height = AttributeParser.parseInt(attributes, "height", 150);

        String src = AttributeParser.parseString(attributes, "src", null);
        MCEFBrowser browser = null;
        if (src != null) {
            try {
                URI uri = URI.create(src);
                Asserts.check(uri.getScheme().equals("https"), "Only https is supported for iframes");
                Asserts.check(ALLOWED_HOSTS.contains(uri.getHost()), "Host is not allowed");

                browser = MCEF.createBrowser(src, false, this.width, this.height);
                browser.setCursorChangeListener(cursor -> this.cursor = switch (cursor) {
                    case 1 -> CursorScreen.Cursor.CROSSHAIR;
                    case 2 -> CursorScreen.Cursor.POINTER;
                    case 3, 29 -> CursorScreen.Cursor.TEXT;
                    case 6, 13, 15 -> CursorScreen.Cursor.RESIZE_EW;
                    case 7, 10, 14 -> CursorScreen.Cursor.RESIZE_NS;
                    case 8, 12, 16 -> CursorScreen.Cursor.RESIZE_NESW;
                    case 9, 11, 17 -> CursorScreen.Cursor.RESIZE_NWSE;
                    case 28 -> CursorScreen.Cursor.RESIZE_ALL;
                    case 34, 37 -> CursorScreen.Cursor.DISABLED;
                    default -> CursorScreen.Cursor.DEFAULT;
                });
            } catch (Exception ignored) {
            }
        }
        this.browser = browser;
    }

    @Override
    protected void drawElement(float x, float y, float width, float height, float mouseX, float mouseY, HtmlRenderer renderer) {
        if (this.browser == null) return;

        this.lastX = (int) x;
        this.lastY = (int) y;

        boolean hovered = mouseX >= x && mouseX <= x + this.width && mouseY >= y && mouseY <= y + this.height;

        if (this.mouseX != mouseX || this.mouseY != mouseY) {
            this.mouseX = (int) mouseX;
            this.mouseY = (int) mouseY;
            this.browser.sendMouseMove(this.mouseX - (int) x, this.mouseY - (int) y);
        }

        this.browser.setFocus(hovered);
        renderer.blit(this.browser.getRenderer().getTextureID(), x, y, this.width, this.height);

        if (hovered) {
            renderer.setCursor(this.cursor);
        }
    }

    @Override
    public void onMouseClickedInternal(MouseButton button, float mouseX, float mouseY) {
        if (this.browser == null) return;

        int relMouseX = (int) mouseX - this.lastX;
        int relMouseY = (int) mouseY - this.lastY;

        this.browser.sendMousePress(relMouseX, relMouseY, switch (button) {
            case LEFT -> InputConstants.MOUSE_BUTTON_LEFT;
            case MIDDLE -> InputConstants.MOUSE_BUTTON_MIDDLE;
            case RIGHT -> InputConstants.MOUSE_BUTTON_RIGHT;
        });

        this.browser.sendMouseRelease(relMouseX, relMouseY, switch (button) {
            case LEFT -> InputConstants.MOUSE_BUTTON_LEFT;
            case MIDDLE -> InputConstants.MOUSE_BUTTON_MIDDLE;
            case RIGHT -> InputConstants.MOUSE_BUTTON_RIGHT;
        });
    }

    @Override
    public void close() {
        if (this.browser == null) return;
        this.browser.close();
    }

    @Override
    protected float getWidth(LayoutData layoutData, HtmlRenderer renderer) {
        return this.width;
    }

    @Override
    protected float getHeight(LayoutData layoutData, HtmlRenderer renderer) {
        return this.height;
    }
}
