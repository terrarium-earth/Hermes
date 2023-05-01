package earth.terrarium.hermes.api.defaults;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teamresourceful.resourcefullib.client.scissor.ScissorBoxStack;
import com.teamresourceful.resourcefullib.client.utils.RenderUtils;
import earth.terrarium.hermes.api.TagElement;
import earth.terrarium.hermes.api.themes.Theme;
import earth.terrarium.hermes.utils.ElementParsingUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ImageTagElement implements TagElement {

    protected List<TagElement> children = new ArrayList<>();
    protected String content;

    private final ResourceLocation image;
    private final int imageWidth;
    private final int imageHeight;
    private final int imageU;
    private final int imageV;
    private final int imageTextureWidth;
    private final int imageTextureHeight;

    public ImageTagElement(Map<String, String> parameters) {
        this.image = ElementParsingUtils.parseResourceLocation(parameters, "src", new ResourceLocation("textures/missing_no.png"));
        this.imageWidth = ElementParsingUtils.parseInt(parameters, "width", 0);
        this.imageHeight = ElementParsingUtils.parseInt(parameters, "height", 0);
        this.imageU = ElementParsingUtils.parseInt(parameters, "u", 0);
        this.imageV = ElementParsingUtils.parseInt(parameters, "v", 0);
        this.imageTextureWidth = ElementParsingUtils.parseInt(parameters, "textureWidth", this.imageWidth);
        this.imageTextureHeight = ElementParsingUtils.parseInt(parameters, "textureHeight", this.imageHeight);
    }

    @Override
    public void render(Theme theme, PoseStack pose, ScissorBoxStack scissor, int x, int y, int width, int mouseX, int mouseY, boolean hovered, float partialTicks) {
        int xOffset = (width - this.imageWidth) / 2;
        RenderUtils.bindTexture(this.image);
        Gui.blit(pose, x + xOffset, y + 2, this.imageU, this.imageV, this.imageWidth, this.imageHeight, this.imageTextureWidth, this.imageTextureHeight);
        if (this.children.size() > 0) {
            int height = this.imageHeight + 4;
            for (TagElement element : this.children) {
                element.render(theme, pose, scissor, x, y + height, width, mouseX, mouseY, hovered, partialTicks);
                height += element.getHeight(width);
            }
        }
    }

    @Override
    public int getHeight(int width) {
        if (this.children.size() > 0) {
            int height = this.imageHeight + 4;
            for (TagElement element : this.children) {
                height += element.getHeight(width);
            }
            return height + 2;
        }
        return this.imageHeight + 4;
    }

    @Override
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public void addChild(TagElement element) {
        if (!(element instanceof TextTagElement)) {
            throw new IllegalArgumentException("Only text elements can be added to a paragraph.");
        }
        this.children.add(element);
    }

    @Override
    public @NotNull List<TagElement> getChildren() {
        return this.children;
    }
}
