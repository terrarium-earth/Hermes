package earth.terrarium.hermes.api.text;

import earth.terrarium.hermes.api.TagElement;
import earth.terrarium.hermes.api.TagProvider;

public final class TextTagProvider extends TagProvider {

    public static final TextTagProvider INSTANCE = new TextTagProvider();

    public TextTagProvider() {
        addSerializer("black", TextTagElements::black);
        addSerializer("dark_blue", TextTagElements::darkBlue);
        addSerializer("dark_green", TextTagElements::darkGreen);
        addSerializer("dark_aqua", TextTagElements::darkAqua);
        addSerializer("dark_red", TextTagElements::darkRed);
        addSerializer("dark_purple", TextTagElements::darkPurple);
        addSerializer("gold", TextTagElements::gold);
        addSerializer("gray", TextTagElements::gray);
        addSerializer("dark_gray", TextTagElements::darkGray);
        addSerializer("blue", TextTagElements::blue);
        addSerializer("green", TextTagElements::green);
        addSerializer("aqua", TextTagElements::aqua);
        addSerializer("red", TextTagElements::red);
        addSerializer("light_purple", TextTagElements::lightPurple);
        addSerializer("yellow", TextTagElements::yellow);
        addSerializer("white", TextTagElements::white);
        addSerializer("obfuscated", TextTagElements::obfuscated);
        addSerializer("obf", TextTagElements::obfuscated);
        addSerializer("!obfuscated", TextTagElements::notObfuscated);
        addSerializer("!obf", TextTagElements::notObfuscated);
        addSerializer("bold", TextTagElements::bold);
        addSerializer("b", TextTagElements::bold);
        addSerializer("!bold", TextTagElements::notBold);
        addSerializer("!b", TextTagElements::notBold);
        addSerializer("strikethrough", TextTagElements::strikethrough);
        addSerializer("st", TextTagElements::strikethrough);
        addSerializer("!strikethrough", TextTagElements::notStrikethrough);
        addSerializer("!st", TextTagElements::notStrikethrough);
        addSerializer("underline", TextTagElements::underlined);
        addSerializer("u", TextTagElements::underlined);
        addSerializer("!underline", TextTagElements::notUnderlined);
        addSerializer("!u", TextTagElements::notUnderlined);
        addSerializer("italic", TextTagElements::italic);
        addSerializer("i", TextTagElements::italic);
        addSerializer("em", TextTagElements::italic);
        addSerializer("!italic", TextTagElements::notItalic);
        addSerializer("!i", TextTagElements::notItalic);
        addSerializer("!em", TextTagElements::notItalic);
        addSerializer("reset", TextTagElements::reset);

        addSerializer("color", TextTagElements::color);
        addSerializer("link", TextTagElements::link);
        addSerializer("clipboard", TextTagElements::copyToClipboard);

        addSerializer("translate", TextTagElements::translate);
        addSerializer("keybind", TextTagElements::keybind);
    }

    @Override
    public TagElement praseTextNode(String text) {
        StyledTagElement element = new StyledTagElement(s -> s);
        element.addText(text);
        return element;
    }
}
