package earth.terrarium.hermes.elements.html;

import java.util.Set;

public class Html {

    public static final Set<String> BLOCK = Set.of(
            "address", "article", "aside", "button", "canvas",
            "div", "figcaption", "figure", "footer", "header",
            "hgroup", "main", "nav", "search", "section", "menu"
    );

    public static final Set<String> NO_DISPLAY = Set.of(
            "col", "colgroup", "datalist", "head",
            "optgroup", "option", // These will be handled by the select element
            "param", "source", // These are handled by the media elements
            "noscript", "script", "style", // These will be handled by script and css if implemented
            "template", "title",
            "dialog"
    );
}
