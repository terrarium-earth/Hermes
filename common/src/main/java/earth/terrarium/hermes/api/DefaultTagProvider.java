package earth.terrarium.hermes.api;

import earth.terrarium.hermes.api.defaults.*;
import earth.terrarium.hermes.api.defaults.carousel.CarouselItemTagElement;
import earth.terrarium.hermes.api.defaults.carousel.CarouselTagElement;
import earth.terrarium.hermes.api.defaults.columns.ColumnTagElement;
import earth.terrarium.hermes.api.defaults.columns.ColumnsTagElement;
import earth.terrarium.hermes.api.defaults.lists.ListItemTagElement;
import earth.terrarium.hermes.api.defaults.lists.OrderedListTagElement;
import earth.terrarium.hermes.api.defaults.lists.UnorderedListTagElement;

public class DefaultTagProvider extends TagProvider {

    public DefaultTagProvider() {
        addSerializer("p", ParagraphTagElement::new);
        addSerializer("h1", HeadingOneTagElement::new);
        addSerializer("h2", HeadingTwoTagElement::new);
        addSerializer("img", ImageTagElement::new);
        addSerializer("carousel", CarouselTagElement::new);
        addSerializer("carousel-item", CarouselItemTagElement::new);
        addSerializer("br", BreakLineTagElement::new);
        addSerializer("blockquote", BlockquoteTagElement::new);
        addSerializer("component", ComponentTagElement::new);
        addSerializer("hint", HintTagElement::new);
        addSerializer("crafting-recipe", CraftingRecipeTagElement::new);
        addSerializer("details", DetailsTagElement::new);
        addSerializer("entity", EntityTagElement::new);
        addSerializer("item", ItemTagElement::new);
        addSerializer("hr", HorizontalRuleTagElement::new);
        addSerializer("ul", UnorderedListTagElement::new);
        addSerializer("ol", OrderedListTagElement::new);
        addSerializer("li", ListItemTagElement::new);
        addSerializer("columns", ColumnsTagElement::new);
        addSerializer("column", ColumnTagElement::new);
    }
}
