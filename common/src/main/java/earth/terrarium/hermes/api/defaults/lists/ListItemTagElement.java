package earth.terrarium.hermes.api.defaults.lists;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teamresourceful.resourcefullib.client.scissor.ScissorBoxStack;
import com.teamresourceful.resourcefullib.common.color.Color;
import com.teamresourceful.resourcefullib.common.color.ConstantColors;
import earth.terrarium.hermes.api.TagElement;
import earth.terrarium.hermes.api.themes.Theme;
import earth.terrarium.hermes.utils.ElementParsingUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListItemTagElement implements TagElement {

    protected final List<TagElement> children = new ArrayList<>();
    protected final Color color;
    protected String content;

    public ListItemTagElement(Map<String, String> parameters) {
        this.color = ElementParsingUtils.parseColor(parameters, "color", ConstantColors.white);
    }

    @Override
    public void render(Theme theme, PoseStack pose, ScissorBoxStack scissor, int x, int y, int width, int mouseX, int mouseY, boolean hovered, float partialTicks) {
        if (this.content != null) {
            Component text = Component.nullToEmpty(this.content);
            int height = 0;
            for (FormattedCharSequence sequence : Minecraft.getInstance().font.split(text, width)) {
                Minecraft.getInstance().font.draw(pose, sequence, x, y + height, this.color.getValue());
                height += Minecraft.getInstance().font.lineHeight + 1;
            }
        } else {
            for (TagElement element : this.children) {
                element.render(theme, pose, scissor, x, y, width, mouseX, mouseY, hovered, partialTicks);
            }
        }
    }

    @Override
    public int getHeight(int width) {
        if (this.content != null) {
            Component text = Component.nullToEmpty(this.content);
            return Minecraft.getInstance().font.split(text, width).size() * (Minecraft.getInstance().font.lineHeight + 1);
        } else {
            int height = 0;
            for (TagElement element : this.children) {
                height += element.getHeight(width);
            }
            return height;
        }
    }

    @Override
    public void setContent(String content) {
        if (!this.children.isEmpty()) {
            throw new IllegalStateException("Cannot set content of a list item that already has children.");
        }
        this.content = content;
    }

    @Override
    public void addChild(TagElement element) {
        if (this.content != null) {
            throw new IllegalStateException("Cannot add a child to a list item that already has content.");
        }
        this.children.add(element);
    }

    @Override
    public @NotNull List<TagElement> getChildren() {
        return this.children;
    }
}
