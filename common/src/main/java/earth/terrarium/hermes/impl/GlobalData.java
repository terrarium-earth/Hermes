package earth.terrarium.hermes.impl;

import earth.terrarium.hermes.api.data.HtmlData;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.Map;

public final class GlobalData implements HtmlData {

    private final Map<Key<?>, Map<String, Object>> data = new HashMap<>();

    @Override
    public <T> void put(Key<T> key, String id, T value) {
        data.computeIfAbsent(key, k -> new HashMap<>()).put(id, value);
    }

    @Override
    public <T> T get(Key<T> key, String id) {
        if (!data.containsKey(key)) return null;
        return key.type().cast(data.get(key).get(id));
    }

    @ApiStatus.Internal
    public void clear() {
        data.clear();
    }
}
