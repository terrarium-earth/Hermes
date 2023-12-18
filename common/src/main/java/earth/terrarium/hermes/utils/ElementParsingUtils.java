package earth.terrarium.hermes.utils;

import com.teamresourceful.resourcefullib.common.color.Color;
import earth.terrarium.hermes.api.Alignable;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

import java.util.Map;

public final class ElementParsingUtils implements Alignable {

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
                return Alignable.alignmentFromString(parameters.get(key).toUpperCase());
            } catch (Exception e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

}
