package earth.terrarium.hermes.api.text;

import com.teamresourceful.resourcefullib.common.color.Color;
import earth.terrarium.hermes.utils.ElementParsingUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.Map;
import java.util.function.UnaryOperator;

public final class TextTagElements {

    public static ChildTextTagElement black(Map<String, String> ignored) {
        return new StyledTagElement(style -> style.applyFormat(ChatFormatting.BLACK));
    }

    public static ChildTextTagElement darkBlue(Map<String, String> ignored) {
        return new StyledTagElement(style -> style.applyFormat(ChatFormatting.DARK_BLUE));
    }

    public static ChildTextTagElement darkGreen(Map<String, String> ignored) {
        return new StyledTagElement(style -> style.applyFormat(ChatFormatting.DARK_GREEN));
    }

    public static ChildTextTagElement darkAqua(Map<String, String> ignored) {
        return new StyledTagElement(style -> style.applyFormat(ChatFormatting.DARK_AQUA));
    }

    public static ChildTextTagElement darkRed(Map<String, String> ignored) {
        return new StyledTagElement(style -> style.applyFormat(ChatFormatting.DARK_RED));
    }

    public static ChildTextTagElement darkPurple(Map<String, String> ignored) {
        return new StyledTagElement(style -> style.applyFormat(ChatFormatting.DARK_PURPLE));
    }

    public static ChildTextTagElement gold(Map<String, String> ignored) {
        return new StyledTagElement(style -> style.applyFormat(ChatFormatting.GOLD));
    }

    public static ChildTextTagElement gray(Map<String, String> ignored) {
        return new StyledTagElement(style -> style.applyFormat(ChatFormatting.GRAY));
    }

    public static ChildTextTagElement darkGray(Map<String, String> ignored) {
        return new StyledTagElement(style -> style.applyFormat(ChatFormatting.DARK_GRAY));
    }

    public static ChildTextTagElement blue(Map<String, String> ignored) {
        return new StyledTagElement(style -> style.applyFormat(ChatFormatting.BLUE));
    }

    public static ChildTextTagElement green(Map<String, String> ignored) {
        return new StyledTagElement(style -> style.applyFormat(ChatFormatting.GREEN));
    }

    public static ChildTextTagElement aqua(Map<String, String> ignored) {
        return new StyledTagElement(style -> style.applyFormat(ChatFormatting.AQUA));
    }

    public static ChildTextTagElement red(Map<String, String> ignored) {
        return new StyledTagElement(style -> style.applyFormat(ChatFormatting.RED));
    }

    public static ChildTextTagElement lightPurple(Map<String, String> ignored) {
        return new StyledTagElement(style -> style.applyFormat(ChatFormatting.LIGHT_PURPLE));
    }

    public static ChildTextTagElement yellow(Map<String, String> ignored) {
        return new StyledTagElement(style -> style.applyFormat(ChatFormatting.YELLOW));
    }

    public static ChildTextTagElement white(Map<String, String> ignored) {
        return new StyledTagElement(style -> style.applyFormat(ChatFormatting.WHITE));
    }

    public static ChildTextTagElement obfuscated(Map<String, String> ignored) {
        return new StyledTagElement(style -> style.withObfuscated(true));
    }

    public static ChildTextTagElement notObfuscated(Map<String, String> ignored) {
        return new StyledTagElement(style -> style.withObfuscated(false));
    }

    public static ChildTextTagElement bold(Map<String, String> ignored) {
        return new StyledTagElement(style -> style.withBold(true));
    }

    public static ChildTextTagElement notBold(Map<String, String> ignored) {
        return new StyledTagElement(style -> style.withBold(false));
    }

    public static ChildTextTagElement strikethrough(Map<String, String> ignored) {
        return new StyledTagElement(style -> style.withStrikethrough(true));
    }

    public static ChildTextTagElement notStrikethrough(Map<String, String> ignored) {
        return new StyledTagElement(style -> style.withStrikethrough(false));
    }

    public static ChildTextTagElement underlined(Map<String, String> ignored) {
        return new StyledTagElement(style -> style.withUnderlined(true));
    }

    public static ChildTextTagElement notUnderlined(Map<String, String> ignored) {
        return new StyledTagElement(style -> style.withUnderlined(false));
    }

    public static ChildTextTagElement italic(Map<String, String> ignored) {
        return new StyledTagElement(style -> style.withItalic(true));
    }

    public static ChildTextTagElement notItalic(Map<String, String> ignored) {
        return new StyledTagElement(style -> style.withItalic(false));
    }

    public static ChildTextTagElement reset(Map<String, String> ignored) {
        return new StyledTagElement(style -> style.applyFormat(ChatFormatting.RESET));
    }

    public static ChildTextTagElement color(Map<String, String> parameters) {
        try {
            final Color color = Color.parse(parameters.get("color"));
            return new StyledTagElement(style -> style.withColor(color.getValue()));
        } catch (Exception ignored) {
            return new StyledTagElement(UnaryOperator.identity());
        }
    }

    public static ChildTextTagElement link(Map<String, String> parameters) {
        String link = parameters.get("href");
        ClickEvent event = new ClickEvent(ClickEvent.Action.OPEN_URL, link);
        HoverEvent hover = new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal(link).withStyle(ChatFormatting.GRAY));
        return new DefaultStyledTagElement(
                style -> style.withClickEvent(event).withHoverEvent(hover),
                Component.literal(link).withStyle(ChatFormatting.BLUE)
        );
    }

    public static ChildTextTagElement copyToClipboard(Map<String, String> parameters) {
        Component text = Component.literal("Copy to Clipboard").withStyle(ChatFormatting.GRAY);
        ClickEvent click = new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, parameters.get("text"));
        HoverEvent hover = new HoverEvent(HoverEvent.Action.SHOW_TEXT, text);
        return new DefaultStyledTagElement(
                style -> style.withClickEvent(click).withHoverEvent(hover),
                text
        );
    }

    public static ChildTextTagElement item(Map<String, String> parameters) {
        Item item = ElementParsingUtils.parseItem(parameters, "item", Items.AIR);
        HoverEvent hover = new HoverEvent(HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemStackInfo(item.getDefaultInstance()));
        return new DefaultStyledTagElement(
                style -> style.withHoverEvent(hover),
                item.getName(item.getDefaultInstance())
        );
    }

    public static ChildTextTagElement translate(Map<String, String> ignored) {
        return new TranslatedTagElement();
    }

    public static ChildTextTagElement keybind(Map<String, String> ignored) {
        return new KeyTagElement();
    }
}
