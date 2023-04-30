package earth.terrarium.hermes.api.defaults;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teamresourceful.resourcefullib.client.scissor.ScissorBoxStack;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.Map;

public class ParagraphTagElement extends TextTagElement {

    public ParagraphTagElement(Map<String, String> parameters) {
        super(parameters);
    }

    @Override
    public void render(PoseStack pose, ScissorBoxStack scissor, int x, int y, int width, int mouseX, int mouseY, float partialTicks) {
        Component text = Component.nullToEmpty(this.content);
        int height = 0;
        for (FormattedCharSequence sequence : Minecraft.getInstance().font.split(text, width - 10)) {
            if (this.shadowed) {
                Minecraft.getInstance().font.drawShadow(pose, sequence, getXOffset(x, width, sequence), y + height, this.color.getValue());
            } else {
                Minecraft.getInstance().font.draw(pose, sequence, getXOffset(x, width, sequence), y + height, this.color.getValue());
            }
            height += Minecraft.getInstance().font.lineHeight + 1;
        }
    }

    @Override
    public int getHeight(int width) {
        int lines = Minecraft.getInstance().font.split(Component.nullToEmpty(this.content), width - 10).size();
        return lines * (Minecraft.getInstance().font.lineHeight + 1);
    }
}
