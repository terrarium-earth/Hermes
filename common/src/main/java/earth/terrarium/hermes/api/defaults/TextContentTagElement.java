package earth.terrarium.hermes.api.defaults;

import com.teamresourceful.resourcefullib.common.color.Color;
import earth.terrarium.hermes.api.TagElement;
import earth.terrarium.hermes.api.TagProvider;
import earth.terrarium.hermes.api.text.ChildTextTagElement;
import earth.terrarium.hermes.api.text.TextTagProvider;
import earth.terrarium.hermes.api.themes.Theme;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.FormattedCharSequence;

import java.util.Map;

public class TextContentTagElement implements TagElement {

    protected MutableComponent component = Component.empty();

    public TextContentTagElement(Map<String, String> ignored) {

    }

    @Override
    public void render(Theme theme, GuiGraphics graphics, int x, int y, int width, int mouseX, int mouseY, boolean hovered, float partialTicks) {
        int height = 0;
        for (FormattedCharSequence sequence : Minecraft.getInstance().font.split(component, width - 5)) {
            theme.drawText(graphics, sequence, x, y + height, Color.DEFAULT, true);
            height += Minecraft.getInstance().font.lineHeight + 1;
        }
    }

    @Override
    public int getHeight(int width) {
        int lines = Minecraft.getInstance().font.split(component, width - 5).size();
        return lines * (Minecraft.getInstance().font.lineHeight + 1);
    }

    @Override
    public void addText(String content) {
        this.component.append(content);
    }

    @Override
    public void addChild(TagElement element) {
        if (element instanceof TextContentTagElement textTag) {
            this.component.append(textTag.component);
        } else if (element instanceof ChildTextTagElement textTag) {
            this.component.append(textTag.component());
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public TagProvider getChildTagProvider(TagProvider parent) {
        return TextTagProvider.INSTANCE;
    }
}
