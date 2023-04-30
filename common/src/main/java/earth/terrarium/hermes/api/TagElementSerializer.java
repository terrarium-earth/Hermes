package earth.terrarium.hermes.api;

import java.util.Map;

@FunctionalInterface
public interface TagElementSerializer {

    TagElement deserialize(Map<String, String> parameters);
}
