package earth.terrarium.hermes.api.defaults;

import com.teamresourceful.resourcefullib.client.utils.ScreenUtils;
import earth.terrarium.hermes.api.TagElement;
import earth.terrarium.hermes.api.themes.Theme;
import earth.terrarium.hermes.utils.ElementParsingUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class CraftingRecipeTagElement implements TagElement {

    protected ResourceLocation id;
    private final int gridWidth;

    public CraftingRecipeTagElement(Map<String, String> parameters) {
        this.id = ElementParsingUtils.parseResourceLocation(parameters, "id", null);
        this.gridWidth = ElementParsingUtils.parseInt(parameters, "grid-width", 3);
    }

    @Override
    public void render(Theme theme, GuiGraphics graphics, int x, int y, int width, int mouseX, int mouseY, boolean hovered, float partialTicks) {
        y += 1;

        int gridSize = this.gridWidth * 18;
        int actualWidth = 5 + gridSize + 5 + 22 + 5 + 18 + 5;
        int actualHeight = gridSize + 10;

        x = x + (width - actualWidth) / 2;

        theme.drawCraftingBackground(graphics, x, y, actualWidth, actualHeight);

        Recipe<?> recipe = getRecipe();
        if (recipe == null) return;

        int xIndex = 0;
        int yIndex = 0;

        for (int i = 0; i < this.gridWidth * this.gridWidth; i++) {
            if (xIndex >= this.gridWidth) {
                xIndex = 0;
                yIndex++;
            }

            boolean slotHovered = hovered && mouseX >= x + 5 + xIndex * 18 && mouseX <= x + 21 + xIndex * 18 && mouseY >= y + 5 + yIndex * 18 && mouseY <= y + 21 + yIndex * 18;

            theme.drawSlot(graphics, x + 5 + xIndex * 18, y + 5 + yIndex * 18, slotHovered);

            if (i < recipe.getIngredients().size()) {
                Ingredient ingredient = recipe.getIngredients().get(i);
                if (!ingredient.isEmpty()) {
                    ItemStack[] stacks = ingredient.getItems();
                    int index = Mth.floor(System.currentTimeMillis() / 1000f) % 100000;
                    ItemStack stack = stacks[index % stacks.length];

                    int slotX = x + 6 + xIndex * 18;
                    int slotY = y + 6 + yIndex * 18;
                    if (slotHovered) {
                        ScreenUtils.setTooltip(stack);
                    }
                    graphics.renderItem(stack, slotX, slotY);
                    graphics.renderItemDecorations(Minecraft.getInstance().font, stack, slotX, slotY);
                }
            }
            xIndex++;
        }

        theme.drawArrow(graphics, x + 5 + gridSize + 5, y + 5 + (gridSize / 2) - 9);

        //noinspection DataFlowIssue
        ItemStack output = recipe.getResultItem(Minecraft.getInstance().getConnection().registryAccess());

        int slotX = x + 5 + gridSize + 5 + 22 + 5;
        int slotY = y + 5 + (gridSize / 2) - 9;

        boolean slotHovered = hovered && mouseX >= slotX + 1 && mouseX <= slotX + 1 + 16 && mouseY >= slotY + 1 && mouseY <= slotY + 1 + 16;

        theme.drawSlot(graphics, slotX, slotY, slotHovered);

        slotY++;
        slotX++;

        if (slotHovered) {
            ScreenUtils.setTooltip(output);
        }

        graphics.renderItem(output, slotX, slotY);
        graphics.renderItemDecorations(Minecraft.getInstance().font, output, slotX, slotY);
    }

    @Nullable
    private Recipe<?> getRecipe() {
        if (id != null && Minecraft.getInstance().level != null && Minecraft.getInstance().getConnection() != null) {
            return Minecraft.getInstance().level.getRecipeManager().byKey(id).orElse(null);
        }
        return null;
    }

    @Override
    public int getHeight(int width) {
        return (18 * this.gridWidth) + 12;
    }
}
