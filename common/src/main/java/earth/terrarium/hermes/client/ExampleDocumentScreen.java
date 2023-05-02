package earth.terrarium.hermes.client;

import com.mojang.blaze3d.vertex.PoseStack;
import earth.terrarium.hermes.api.TagProvider;
import earth.terrarium.hermes.api.defaults.*;
import earth.terrarium.hermes.api.defaults.carousel.CarouselItemTagElement;
import earth.terrarium.hermes.api.defaults.columns.ColumnTagElement;
import earth.terrarium.hermes.api.defaults.columns.ColumnsTagElement;
import earth.terrarium.hermes.api.defaults.carousel.CarouselTagElement;
import earth.terrarium.hermes.api.defaults.lists.UnorderedListTagElement;
import earth.terrarium.hermes.api.defaults.lists.ListItemTagElement;
import earth.terrarium.hermes.api.defaults.lists.OrderedListTagElement;
import earth.terrarium.hermes.api.themes.DefaultTheme;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import org.jetbrains.annotations.NotNull;

public class ExampleDocumentScreen extends Screen {

    public ExampleDocumentScreen() {
        super(CommonComponents.EMPTY);
    }

    @Override
    protected void init() {
        super.init();
        String text = """
            <p>Column Test</p>
            <carousel height="100" index="1">
                <carousel-item>
                    <p>Test</p>
                </carousel-item>
                <carousel-item>
                    <img src="minecraft:textures/block/stone.png" width="100" height="100" textureWidth="16" textureHeight="16"/>
                </carousel-item>
            </carousel>
            """;
        TagProvider provider = new TagProvider();
        provider.addSerializer("p", ParagraphTagElement::new);
        provider.addSerializer("h1", HeadingOneTagElement::new);
        provider.addSerializer("h2", HeadingTwoTagElement::new);
        provider.addSerializer("img", ImageTagElement::new);
        provider.addSerializer("carousel", CarouselTagElement::new);
        provider.addSerializer("carousel-item", CarouselItemTagElement::new);
        provider.addSerializer("br", BreakLineTagElement::new);
        provider.addSerializer("blockquote", BlockquoteTagElement::new);
        provider.addSerializer("component", ComponentTagElement::new);
        provider.addSerializer("hint", HintTagElement::new);
        provider.addSerializer("crafting-recipe", CraftingRecipeTagElement::new);
        provider.addSerializer("details", DetailsTagElement::new);
        provider.addSerializer("entity", EntityTagElement::new);
        provider.addSerializer("hr", HorizontalRuleTagElement::new);
        provider.addSerializer("ul", UnorderedListTagElement::new);
        provider.addSerializer("ol", OrderedListTagElement::new);
        provider.addSerializer("li", ListItemTagElement::new);
        provider.addSerializer("columns", ColumnsTagElement::new);
        provider.addSerializer("column", ColumnTagElement::new);
        addRenderableWidget(new DocumentWidget(this.width / 2 - 100, 10, 200, 200, new DefaultTheme(), provider.parse(text)));
    }

    @Override
    public void render(@NotNull PoseStack stack, int i, int j, float f) {
        this.renderBackground(stack);
        super.render(stack, i, j, f);
    }
}
