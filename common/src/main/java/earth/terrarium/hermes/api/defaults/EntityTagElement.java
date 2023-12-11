package earth.terrarium.hermes.api.defaults;

import earth.terrarium.hermes.api.TagElement;
import earth.terrarium.hermes.api.themes.Theme;
import earth.terrarium.hermes.utils.ElementParsingUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;

import java.util.Map;

public class EntityTagElement implements TagElement {

    private final EntityType<?> type;
    private final CompoundTag tag;
    private final static int BLOCK_HEIGHT = 25;
    private final float scale;
    private float height;
    private Entity entity;

    public EntityTagElement(Map<String, String> parameters) {
        this.type = ElementParsingUtils.parseEntityType(parameters, "type", null);
        this.tag = ElementParsingUtils.parseTag(parameters, "tag", null);
        this.scale = ElementParsingUtils.parseFloat(parameters, "scale", 1.0f);
        this.height = ElementParsingUtils.parseFloat(parameters, "height", 0.0f);
    }

    @Override
    public void render(Theme theme, GuiGraphics graphics, int x, int y, int width, int mouseX, int mouseY, boolean hovered, float partialTicks) {
        if (this.type != null) {
            if (entity == null && Minecraft.getInstance().level != null) {
                entity = this.type.create(Minecraft.getInstance().level);
                if (entity != null && tag != null) {
                    entity.load(tag);
                }
            }
            if (entity instanceof LivingEntity living) {
                if (height == 0.0f) {
                    height = living.getBbHeight();
                }
                int blockScale = (int) ((BLOCK_HEIGHT * scale) + 0.5f);
                int offsetX = x + (int) ((width / 2f) + 0.5f);
                int offsetY = y + (int) ((height * blockScale) + 0.5f);
                int eyeOffset = offsetY - (int) ((living.getEyeHeight() * blockScale) + 0.5f);
                InventoryScreen.renderEntityInInventoryFollowsMouse(graphics, offsetX, offsetY, blockScale, offsetX - mouseX, eyeOffset - mouseY, living);
            }
        }
    }

    @Override
    public int getHeight(int width) {
        return (int) ((height * BLOCK_HEIGHT * scale) + 3 + 0.5f);
    }
}
