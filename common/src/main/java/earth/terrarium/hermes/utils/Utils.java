package earth.terrarium.hermes.utils;

import com.mojang.blaze3d.platform.NativeImage;
import org.joml.Vector4f;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public class Utils {

    public static Vector4f toVec4f(int color) {
        return new Vector4f(
                (color >> 16 & 0xFF) / 255f,
                (color >> 8 & 0xFF) / 255f,
                (color & 0xFF) / 255f,
                (color >> 24 & 0xFF) / 255f
        );
    }


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

    public static NativeImage toNative(BufferedImage image) {
        NativeImage nativeImage = new NativeImage(image.getWidth(), image.getHeight(), true);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int argb = image.getRGB(x, y);
                int abgr = (argb & 0xFF00FF00) | ((argb & 0xFF) << 16) | ((argb >> 16) & 0xFF);
                nativeImage.setPixelRGBA(x, y, abgr);
            }
        }
        return nativeImage;
    }
}
