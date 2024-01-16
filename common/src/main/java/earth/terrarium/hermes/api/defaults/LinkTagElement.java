package earth.terrarium.hermes.api.defaults;

import earth.terrarium.hermes.utils.ElementParsingUtils;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.net.URL;
import java.util.Map;

public class LinkTagElement extends ComponentTagElement {

    protected String href;

    public LinkTagElement(Map<String, String> parameters) {
        super(parameters);
        @Nullable URL url = ElementParsingUtils.parseURL(parameters, "href");
        this.href = (url != null) ? url.toString() : "";
    }

    @Override
    public void setContent(String content) {
        try {
            var linkText = String.format("\"text\":\"%s\"", content);
            var linkClick = String.format("\"clickEvent\":{\"action\":\"open_url\",\"value\":\"%s\"}", href);
            var linkHover = String.format("\"hoverEvent\": {\"action\": \"show_text\", \"value\":\"%s\"}", href);
            var linkJson = "{" + String.join(",", linkText, linkHover, linkClick) + "}";
            this.text = Component.Serializer.fromJson(linkJson);
        } catch (Exception e) {
            this.text = CommonComponents.EMPTY;
        }
    }
    // <component>{"text":"This is a link","clickEvent":{"action":"open_url","value":"https://example.com"}}</component>
}

