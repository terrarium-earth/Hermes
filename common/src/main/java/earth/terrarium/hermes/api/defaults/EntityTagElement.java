package earth.terrarium.hermes.api.defaults;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teamresourceful.resourcefullib.client.scissor.ScissorBoxStack;
import earth.terrarium.hermes.api.TagElement;
import earth.terrarium.hermes.utils.ElementParsingUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;

import java.util.Map;

public class EntityTagElement implements TagElement {

    private final EntityType<?> type;
    private Entity entity;

    public EntityTagElement(Map<String, String> parameters) {
        this.type = ElementParsingUtils.parseEntityType(parameters, "type", null);
    }

    @Override
    public void render(PoseStack pose, ScissorBoxStack scissor, int x, int y, int width, int mouseX, int mouseY, float partialTicks) {
        if (this.type != null) {
            if (entity == null && Minecraft.getInstance().level != null) {
                entity = this.type.create(Minecraft.getInstance().level);
            }
            if (entity instanceof LivingEntity living) {
                var window = Minecraft.getInstance().getWindow();
                InventoryScreen.renderEntityInInventoryFollowsMouse(pose, x + (int)(width / 2f), y + 47, 25, x + (int)(width / 2f) - mouseX, y + 47 - mouseY, living);
            }
        }
    }

    @Override
    public int getHeight(int width) {
        return 50;
    }
}
