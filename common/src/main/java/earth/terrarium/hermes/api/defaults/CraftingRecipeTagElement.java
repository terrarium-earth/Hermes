package earth.terrarium.hermes.api.defaults;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teamresourceful.resourcefullib.client.scissor.ScissorBoxStack;
import earth.terrarium.hermes.api.TagElement;
import earth.terrarium.hermes.client.ClientUtils;
import earth.terrarium.hermes.utils.ElementParsingUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

import java.util.Map;

public class CraftingRecipeTagElement implements TagElement {

    protected ResourceLocation id;
    private final int gridWidth;

    public CraftingRecipeTagElement(Map<String, String> parameters) {
        this.id = ElementParsingUtils.parseResourceLocation(parameters, "id", null);
        this.gridWidth = ElementParsingUtils.parseInt(parameters, "gridWidth", 3);
    }

    @Override
    public void render(PoseStack pose, ScissorBoxStack scissor, int x, int y, int width, int mouseX, int mouseY, float partialTicks) {
        y += 1;
        int gridSize = this.gridWidth * 18;
        int center = (gridSize / 2) - 9;

        int startX = x + (width - 96) / 2;
        Gui.fill(pose, startX, y, startX + gridSize, y + gridSize, 0x80000000);
        Gui.renderOutline(pose, startX, y, gridSize, gridSize, 0xFFFFFFFF);
        Gui.fill(pose, startX + gridSize + 24, y + center, startX + gridSize + 24 + 18, y + center + 18, 0x80000000);
        Gui.renderOutline(pose, startX + gridSize + 24, y + center, 18, 18, 0xFFFFFFFF);

        if (id != null && Minecraft.getInstance().level != null && Minecraft.getInstance().getConnection() != null) {
            Recipe<?> recipe = Minecraft.getInstance().level.getRecipeManager().byKey(id).orElse(null);
            if (recipe == null) return;
            int xIndex = 0;
            int yIndex = 0;

            for (int i = 0; i < Math.min(recipe.getIngredients().size(), this.gridWidth * this.gridWidth); i++) {
                if (xIndex >= this.gridWidth) {
                    xIndex = 0;
                    yIndex++;
                }
                Ingredient ingredient = recipe.getIngredients().get(i);
                ItemStack stack = ingredient.getItems()[0].getItem().getDefaultInstance();
                if (mouseX >= startX + 1 + xIndex * 18 && mouseX <= startX + 17 + xIndex * 18 && mouseY >= y + 1 + yIndex * 18 && mouseY <= y + 17 + yIndex * 18) {
                    Gui.fill(pose, startX + 1 + xIndex * 18, y + 1 + yIndex * 18, startX + 17 + xIndex * 18, y + 17 + yIndex * 18, 0x80FFFFFF);
                    ClientUtils.renderTooltip(stack);
                }
                ClientUtils.renderItemWithCount(pose, stack, startX + 1 + xIndex * 18, y + 1 + yIndex * 18);
                xIndex++;
            }

            if (mouseX >= startX + gridSize + 25 && mouseX <= startX + gridSize + 24 + 18 && mouseY >= y + center && mouseY < y + center + 18) {
                Gui.fill(pose, startX + gridSize + 25, y + center, startX + gridSize + 24 + 18, y + center + 18, 0x80FFFFFF);
                ClientUtils.renderTooltip(recipe.getResultItem(Minecraft.getInstance().getConnection().registryAccess()));
            }
            ClientUtils.renderItemWithCount(pose, recipe.getResultItem(Minecraft.getInstance().getConnection().registryAccess()), startX + gridSize + 25, y + center);
        }
    }

    @Override
    public int getHeight(int width) {
        return (18 * this.gridWidth) + 2;
    }
}
