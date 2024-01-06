package earth.terrarium.hermes.api;

import earth.terrarium.hermes.api.themes.Theme;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface TagElement {

    /**
     * Renders the element.
     *
     * @param theme        The theme.
     * @param graphics     The graphics.
     * @param x            The x position.
     * @param y            The y position.
     * @param width        The width.
     * @param mouseX       The mouse x position.
     * @param mouseY       The mouse y position.
     * @param hovered      Whether the element is hovered, use this along with mouse position to determine if the mouse is hovering over the element,
     *                     as the mouse can be outside the screen space but have the correct position.
     * @param partialTicks The partial ticks.
     */
    default void render(Theme theme, GuiGraphics graphics, int x, int y, int width, int mouseX, int mouseY, boolean hovered, float partialTicks) {

    }

    default int getHeight(int width) {
        return 0;
    }

    default boolean mouseClicked(double mouseX, double mouseY, int button, int width) {
        return false;
    }

    /**
     * @deprecated Override and use {@link #addText(String)} instead.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "1.21")
    default void setContent(String content) {
        throw new UnsupportedOperationException();
    }

    default void addText(String content) {
        setContent(content);
    }

    default void addChild(TagElement element) {
        throw new UnsupportedOperationException();
    }

    @NotNull
    default List<TagElement> getChildren() {
        return List.of();
    }

    /**
     * This method is used to determine the tag provider it should use for the children of this element when parsing.
     * @param parent The tag provider of the parent element.
     * @return The tag provider to use for the children of this element.
     */
    default TagProvider getChildTagProvider(TagProvider parent) {
        return parent;
    }
}
