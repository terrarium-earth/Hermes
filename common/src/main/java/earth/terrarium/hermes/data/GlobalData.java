package earth.terrarium.hermes.data;

import earth.terrarium.hermes.data.map.ImageMap;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.Map;

public class GlobalData {

    public static final Key<ImageMap> IMAGE_MAP = new Key<>(ImageMap.class);

    private final Map<Key<?>, Map<String, Object>> data = new HashMap<>();

    public <T> void put(Key<T> key, String id, T value) {
        data.computeIfAbsent(key, k -> new HashMap<>()).put(id, value);
    }

    public <T> T get(Key<T> key, String id) {
        if (!data.containsKey(key)) return null;
        return key.type.cast(data.get(key).get(id));
    }

    @ApiStatus.Internal
    public void clear() {
        data.clear();
    }

    public static class Key<T> {
        private final Class<T> type;

        public Key(Class<T> type) {
            this.type = type;
        }
    }
}
