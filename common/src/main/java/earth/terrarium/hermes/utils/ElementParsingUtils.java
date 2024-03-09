package earth.terrarium.hermes.utils;

import com.teamresourceful.resourcefullib.common.color.Color;
import earth.terrarium.hermes.api.Alignment;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public final class ElementParsingUtils {

    public static ResourceLocation parseResourceLocation(Map<String, String> parameters, String key, ResourceLocation defaultValue) {
        if (parameters.containsKey(key)) {
            try {
                return new ResourceLocation(parameters.get(key));
            } catch (Exception e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    public static Color parseColor(Map<String, String> parameters, String key, Color defaultValue) {
        if (parameters.containsKey(key)) {
            try {
                return Color.parse(parameters.get(key));
            } catch (Exception e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    public static Item parseItem(Map<String, String> parameters, String key, Item defaultValue) {
        if (parameters.containsKey(key)) {
            try {
                return BuiltInRegistries.ITEM.get(new ResourceLocation(parameters.get(key)));
            } catch (Exception e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    public static EntityType<?> parseEntityType(Map<String, String> parameters, String key, EntityType<?> defaultValue) {
        if (parameters.containsKey(key)) {
            try {
                return BuiltInRegistries.ENTITY_TYPE.get(new ResourceLocation(parameters.get(key)));
            } catch (Exception e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    public static CompoundTag parseTag(Map<String, String> parameters, String key, CompoundTag defaultValue) {
        if (parameters.containsKey(key)) {
            try {
                return TagParser.parseTag(parameters.get(key));
            } catch (Exception e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    public static int parseInt(Map<String, String> parameters, String key, int defaultValue) {
        if (parameters.containsKey(key)) {
            try {
                return Integer.parseInt(parameters.get(key));
            } catch (Exception e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    public static float parseFloat(Map<String, String> parameters, String key, float defaultValue) {
        if (parameters.containsKey(key)) {
            try {
                return Float.parseFloat(parameters.get(key));
            } catch (Exception e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    public static boolean parseBoolean(Map<String, String> parameters, String key, boolean defaultValue) {
        if (parameters.containsKey(key)) {
            try {
                return Boolean.parseBoolean(parameters.get(key));
            } catch (Exception e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    public static Alignment parseAlignment(Map<String, String> parameters, String key, Alignment defaultValue) {
        if (parameters.containsKey(key)) {
            try {
                return Alignment.fromString(parameters.get(key));
            } catch (Exception e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    public static Map<String, String> parseStyleAttribute(Map<String, String> parameters) {

        // Allow for using 'style="..."' attribute values to compactly specify multiple attributes.
        // These compact attributes may be in 'key:value' or boolean form.
        // Booleans can be negated with a leading '!'; both forms may have short names or synonyms.
        // Example: style="!obfuscated,b,em,color:lavender,bg:0 #4a38d0"

        Map<String, String> result = new HashMap<>();
        String[] specificers = parameters.get("style").split(";");
        String specValue;
        for (String specifier : specificers) {
            String[] specParts = specifier.split(":",2);
            if (specParts.length == 2) {
                // key:value form
                specifier = specParts[0].strip();
                specValue = specParts[1].strip();
            } else {
                // boolean form
                specifier = specifier.strip();
                if (specifier.startsWith("!")) {
                    specifier = specifier.substring(1).strip();
                    specValue = "false";
                } else {
                    specValue = "true";
                }
            }
            // handle short names and synonyms
            specifier = switch (specifier) {
                case "i", "em" -> "italic";
                case "b" -> "bold";
                case "u" -> "underline";
                case "st" -> "strikethrough";
                case "obf" -> "obfuscated";
                case "bg" -> "background";
                case "brd" -> "border";
                default -> specifier;
            };
            result.put(specifier, specValue);
        }
        return result;
    }

    public static <A, B, C> @Nullable C parsePair(
            @NotNull Map<String, String> parameters, String key, BiFunction<A, B, C> factory,
            Function<String, A> parserA, A defaultA, Function<String, B> parserB, B defaultB
    ) {
        if (!parameters.containsKey(key)) {
            return null;
        }

        String[] spec = parameters.get(key).split(" ");
        return switch (spec.length) {
            case 0 -> factory.apply(defaultA, defaultB);
            case 1 -> factory.apply(tryParse(spec[0], parserA, defaultA), defaultB);
            case 2 -> factory.apply(tryParse(spec[0], parserA, defaultA), tryParse(spec[1], parserB, defaultB));
            default -> null;
        };
    }

    public static <R> R tryParse(String input, Function<String, R> parseFunc, R defaultResult) {
        try {
            return parseFunc.apply(input);
        } catch (Exception e) {
            return defaultResult;
        }
    }

}
