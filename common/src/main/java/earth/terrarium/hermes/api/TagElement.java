package earth.terrarium.hermes.api;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teamresourceful.resourcefullib.client.scissor.ScissorBoxStack;
import earth.terrarium.hermes.api.themes.Theme;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface TagElement {

    /**
     * Renders the element.
     *
     * @param pose         The pose stack.
     * @param scissor      The scissor box stack.
     * @param x            The x position.
     * @param y            The y position.
     * @param width        The width.
     * @param mouseX       The mouse x position.
     * @param mouseY       The mouse y position.
     * @param partialTicks The partial ticks.
     */
    default void render(Theme theme, PoseStack pose, ScissorBoxStack scissor, int x, int y, int width, int mouseX, int mouseY, float partialTicks) {

    }

    default int getHeight(int width) {
        return 0;
    }

    default boolean mouseClicked(double mouseX, double mouseY, int button, int width) {
        return false;
    }

    default void setContent(String content) {
        throw new UnsupportedOperationException();
    }

    default void addChild(TagElement element) {
        throw new UnsupportedOperationException();
    }

    @NotNull
    default List<TagElement> getChildren() {
        return List.of();
    }
}
