package earth.terrarium.hermes.client;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.CommonComponents;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public abstract class ContainerWidget extends AbstractWidget implements ContainerEventHandler {

    private GuiEventListener focused;
    private boolean isDragging;

    public ContainerWidget(int x, int y, int width, int height) {
        super(x, y, width, height, CommonComponents.EMPTY);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        // DO NOTHING
    }

    @Override
    public boolean isDragging() {
        return this.isDragging;
    }

    @Override
    public void setDragging(boolean isDragging) {
        this.isDragging = isDragging;
    }

    @Nullable
    @Override
    public GuiEventListener getFocused() {
        return this.focused;
    }

    @Override
    public void setFocused(@Nullable GuiEventListener focused) {
        if (this.focused != null) {
            this.focused.setFocused(false);
        }

        if (focused != null) {
            focused.setFocused(true);
        }

        this.focused = focused;
    }
}
