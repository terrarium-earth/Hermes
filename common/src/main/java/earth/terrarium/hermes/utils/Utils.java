package earth.terrarium.hermes.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public class Utils {

    public static <T> T findFirst(Collection<T> collection, Predicate<T> predicate) {
        for (T t : collection) {
            if (predicate.test(t)) {
                return t;
            }
        }
        return null;
    }

    public static <T> List<T> filter(Collection<T> collection, Predicate<T> predicate) {
        List<T> list = new ArrayList<>();
        for (T t : collection) {
            if (predicate.test(t)) {
                list.add(t);
            }
        }
        return list;
    }
}
