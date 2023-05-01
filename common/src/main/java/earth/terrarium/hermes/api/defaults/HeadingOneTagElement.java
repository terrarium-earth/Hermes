package earth.terrarium.hermes.api.defaults;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teamresourceful.resourcefullib.client.scissor.ScissorBoxStack;
import earth.terrarium.hermes.api.themes.Theme;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.Map;

public class HeadingOneTagElement extends TextTagElement {

    public HeadingOneTagElement(Map<String, String> parameters) {
        super(parameters);
    }

    @Override
    public void render(Theme theme, PoseStack pose, ScissorBoxStack scissor, int x, int y, int width, int mouseX, int mouseY, boolean hovered, float partialTicks) {
        pose.pushPose();
        pose.scale(3, 3, 3);
        pose.translate(-x / 1.5f, -y / 1.5f, 0);
        Component text = Component.nullToEmpty(this.content);
        int height = 0;
        for (FormattedCharSequence sequence : Minecraft.getInstance().font.split(text, width - 10)) {
            Minecraft.getInstance().font.draw(pose, sequence, x + 2, y + height, this.color.getValue());
            height += Minecraft.getInstance().font.lineHeight + 1;
        }
        pose.popPose();
    }

    @Override
    public int getHeight(int width) {
        int lines = Minecraft.getInstance().font.split(Component.nullToEmpty(this.content), width - 10).size();
        return lines * (Minecraft.getInstance().font.lineHeight + 1) * 3;
    }
}
