package earth.terrarium.hermes.api.links;

import net.minecraft.Util;

public enum DefaultProtocolHandler implements LinkHandler {
    INSTANCE;

    @Override
    public int priority() {
        return Integer.MIN_VALUE;
    }

    @Override
    public boolean canHandle(String url) {
        return true;
    }

    @Override
    public void handle(String url) {
        Util.getPlatform().openUri(url);
    }
}
