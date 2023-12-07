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
    private final float scale;
    private Entity entity;

    public EntityTagElement(Map<String, String> parameters) {
        this.type = ElementParsingUtils.parseEntityType(parameters, "type", null);
        this.tag = ElementParsingUtils.parseTag(parameters, "tag", null);
        this.scale = ElementParsingUtils.parseFloat(parameters, "scale", 1.0f);
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
            int renderHeight = (int) (25 * scale);
            int offsetY = getHeight(width) - (int) (3 * scale);
            if (entity instanceof LivingEntity living) {
                InventoryScreen.renderEntityInInventoryFollowsMouse(graphics, x + (int) (width / 2f), y + offsetY, renderHeight, x + (int) (width / 2f) - mouseX, y + 47 - mouseY, living);
            }
        }
    }

    @Override
    public int getHeight(int width) {
        return (int) (50 * scale);
    }
}
