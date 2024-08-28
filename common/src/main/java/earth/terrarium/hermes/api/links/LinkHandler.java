package earth.terrarium.hermes.api.links;

import java.util.List;
import java.util.function.Consumer;

public interface LinkHandler {

    int priority();

    boolean canHandle(String url);

    void handle(String url);

    static Consumer<String> tryHandle(List<LinkHandler> handlers) {
        return url -> {
            for (LinkHandler handler : handlers) {
                if (handler.canHandle(url)) {
                    handler.handle(url);
                    return;
                }
            }
        };
    }
}
