package earth.terrarium.hermes.client;

import com.mojang.blaze3d.vertex.PoseStack;
import earth.terrarium.hermes.api.TagProvider;
import earth.terrarium.hermes.api.defaults.*;
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
            <h1 color="#FF0000">Hello World!</h1>
            <p color="#00FF00">This is a test of the Hermes system.</p>
            <h2 color="#0000FF">This is a heading 2</h2>
            <p bold="true">Bold text!</p>
            <details summary="This is a details tag">
                <p>Crafting Recipe Spoiler!</p>
                <details summary="Sub Details">
                    <p>Crafting Recipe Spoiler!</p>
                    <details summary="Details Details">
                        <p>Crafting Recipe Spoiler!</p>
                    </details>
                </details>
            </details>
            <crafting-recipe id="minecraft:acacia_fence"></crafting-recipe>
            <crafting-recipe gridWidth="1" id="minecraft:coal_from_smelting_deepslate_coal_ore"></crafting-recipe>
            <gallery height="32">
                <img width="16" height="16" src="minecraft:textures/gui/advancements/backgrounds/adventure.png">
                    <p centered="true" shadowed="true">captions and stuff</p>
                </img>
                <img width="16" height="16" src="minecraft:textures/gui/advancements/backgrounds/end.png">
                    <p centered="true">End Caption</p>
                </img>
            </gallery>
            <blockquote>
                <p>This is a blockquote</p>
                <p>This is another paragraph in the blockquote</p>
            </blockquote>
            <component>{"text":"Test","hoverEvent":{"action":"show_text","contents":"Your MOTHER IS VERY COOL!"}}</component>
            <br></br>
            <hint icon="minecraft:stick" title="This is a hint" color="#FF0000">
                <p>This is a hint</p>
                <p>This is another paragraph in the hint</p>
            </hint>
            <hr></hr>
            <entity type="minecraft:zombie"></entity>
            """;
        TagProvider provider = new TagProvider();
        provider.addSerializer("p", ParagraphTagElement::new);
        provider.addSerializer("h1", HeadingOneTagElement::new);
        provider.addSerializer("h2", HeadingTwoTagElement::new);
        provider.addSerializer("img", ImageTagElement::new);
        provider.addSerializer("gallery", GalleryTagElement::new);
        provider.addSerializer("br", BreakLineTagElement::new);
        provider.addSerializer("blockquote", BlockquoteTagElement::new);
        provider.addSerializer("component", ComponentTagElement::new);
        provider.addSerializer("hint", HintTagElement::new);
        provider.addSerializer("crafting-recipe", CraftingRecipeTagElement::new);
        provider.addSerializer("details", DetailsTagElement::new);
        provider.addSerializer("entity", EntityTagElement::new);
        provider.addSerializer("hr", HorizontalRuleTagElement::new);
        addRenderableWidget(new DocumentWidget(this.width / 2 - 100, 10, 200, 200, provider.parse(text)));
    }

    @Override
    public void render(@NotNull PoseStack stack, int i, int j, float f) {
        this.renderBackground(stack);
        super.render(stack, i, j, f);
    }
}
