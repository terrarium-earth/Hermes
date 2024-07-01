package earth.terrarium.hermes.utils;

import net.minecraft.Util;

import java.util.Locale;
import java.util.TreeMap;

public class Numerals {

    private final static TreeMap<Integer, String> map = Util.make(new TreeMap<>(), map -> {
        map.put(1000, "M");
        map.put(900, "CM");
        map.put(500, "D");
        map.put(400, "CD");
        map.put(100, "C");
        map.put(90, "XC");
        map.put(50, "L");
        map.put(40, "XL");
        map.put(10, "X");
        map.put(9, "IX");
        map.put(5, "V");
        map.put(4, "IV");
        map.put(1, "I");
    });

    /**
     * Takes a number to convert to a roman numeral representation.
     * ie. 1 -> I, 100 -> C
     */
    public static String toRoman(int number, Casing casing) {
        int l = map.floorKey(number);
        if (number == l) return casing == Casing.UPPER ? map.get(number) : map.get(number).toLowerCase(Locale.ROOT);
        return map.get(l) + toRoman(number - l, casing);
    }

    /**
     * Takes a number to convert to an alphabetic representation.
     * ie. 1 -> A, 100 -> CV
     */
    public static String toAlpha(int number, Casing casing) {
        StringBuilder result = new StringBuilder();
        while (number > 0) {
            number--;
            result.insert(0, (char) ('A' + number % 26));
            number /= 26;
        }
        return casing == Casing.LOWER ? result.toString().toLowerCase(Locale.ROOT) : result.toString();
    }

    public enum Casing {
        LOWER,
        UPPER
    }

}
