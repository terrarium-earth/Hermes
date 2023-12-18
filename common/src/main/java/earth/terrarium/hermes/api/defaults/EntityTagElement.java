package earth.terrarium.hermes.api.defaults;

import earth.terrarium.hermes.api.Alignment;
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
    private final static int BLOCK_HEIGHT = 24;
    private final float scale;
    private float entityBlocksHigh;
    private final Alignment align;
    private float entityBlocksWide;
    private Entity entity;

    public EntityTagElement(Map<String, String> parameters) {
        this.type = ElementParsingUtils.parseEntityType(parameters, "type", null);
        this.tag = ElementParsingUtils.parseTag(parameters, "tag", null);
        this.scale = ElementParsingUtils.parseFloat(parameters, "scale", 1.0f);
        this.entityBlocksHigh = ElementParsingUtils.parseFloat(parameters, "height", 0.0f);
        this.align = ElementParsingUtils.parseAlignment(parameters, "align", Alignment.MIDDLE);
        this.entityBlocksWide = ElementParsingUtils.parseFloat(parameters, "width", 0.0f);
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

                if (entityBlocksHigh == 0.0f) {
                    entityBlocksHigh = living.getBbHeight();
                }
                if (entityBlocksWide == 0.0f) {
                    entityBlocksWide = living.getBbWidth();
                }

                int blockScale = Math.round(scale * BLOCK_HEIGHT);
                float entityHeight = blockScale * entityBlocksHigh;
                float entityWidth = blockScale * entityBlocksWide;

                int offsetX = Alignment.getOffsetCenterDrawnElement(width, entityWidth, align);
                int offsetY = Math.round(entityHeight);
                int eyeOffset = Math.round(entityHeight - (living.getEyeHeight() * blockScale));

                InventoryScreen.renderEntityInInventoryFollowsMouse(graphics, x + offsetX, y + offsetY + 1, blockScale, x + offsetX - mouseX, y + eyeOffset - mouseY, living);
            }
        }
    }

    @Override
    public int getHeight(int width) {
        return Math.round((entityBlocksHigh * scale * BLOCK_HEIGHT) + 2);
    }
}
