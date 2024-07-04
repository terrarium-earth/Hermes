package earth.terrarium.hermes.api.data;

public interface HtmlData {

    <T> void put(Key<T> key, String id, T value);

    <T> T get(Key<T> key, String id);

    record Key<T>(Class<T> type) {}
}
