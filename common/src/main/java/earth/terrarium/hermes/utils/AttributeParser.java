package earth.terrarium.hermes.utils;

import com.mojang.brigadier.StringReader;
import net.minecraft.commands.arguments.item.ItemParser;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import org.xml.sax.Attributes;

public class AttributeParser {

    private static final RegistryAccess STATIC_ACCESS = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);

    public static String parseString(Attributes attributes, String key, String defaultValue) {
        var attribute = attributes.getValue(key);
        return attribute == null ? defaultValue : attribute;
    }

    public static int parseInt(Attributes attributes, String key, int defaultValue) {
        var attribute = attributes.getValue(key);
        if (attribute == null) return defaultValue;
        try {
            return Integer.parseInt(attribute);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static float parseFloat(Attributes attributes, String key, float defaultValue) {
        var attribute = attributes.getValue(key);
        if (attribute == null) return defaultValue;
        try {
            return Float.parseFloat(attribute);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static boolean parseBoolean(Attributes attributes, String key, boolean defaultValues) {
        var attribute = attributes.getValue(key);
        if (attribute == null) return defaultValues;
        return Boolean.parseBoolean(attribute);
    }

    public static ItemStack parseItem(Attributes attributes, String key) {
        var attribute = attributes.getValue(key);
        if (attribute == null) return ItemStack.EMPTY;
        var parser = new ItemParser(STATIC_ACCESS);
        try {
            var result = parser.parse(new StringReader(attribute));
            ItemStack stack = new ItemStack(result.item(), 1);
            stack.applyComponents(result.components());
            return stack;
        } catch (Exception ignored) {
            return ItemStack.EMPTY;
        }
    }

    public static EntityType<?> parseEntityType(Attributes attributes, String key, EntityType<?> defaultValue) {
        var attribute = attributes.getValue(key);
        if (attribute == null) return defaultValue;
        return EntityType.byString(attribute).orElse(defaultValue);
    }

    public static CompoundTag parseNbt(Attributes attributes, String key, CompoundTag defaultValue) {
        var attribute = attributes.getValue(key);
        if (attribute == null) return defaultValue;
        try {
            return TagParser.parseTag(attribute);
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
