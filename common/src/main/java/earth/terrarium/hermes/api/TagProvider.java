package earth.terrarium.hermes.api;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.regex.Pattern;

public class TagProvider {

    private static final Pattern LESS_THAN_REPLACE = Pattern.compile("([^\\\\])\\\\<|^\\\\<");
    private static final Pattern GREATER_THAN_REPLACE = Pattern.compile("([^\\\\])\\\\>|^\\\\>");

    private final Map<String, TagElementSerializer> serializers = new HashMap<>();

    public void addSerializer(String tag, TagElementSerializer serializer) {
        this.serializers.put(tag, serializer);
    }

    public List<TagElement> parse(String text) {
        Stack<TagElement> stack = new Stack<>();
        stack.push(new Root(new ArrayList<>()));
        StringBuilder content = new StringBuilder();

        int index = 0;
        while (index < text.length()) {
            int openIndex = indexOfUnescaped('<', text, index);
            if (openIndex == -1) break;
            int closeIndex = indexOfUnescaped('>', text, openIndex);
            if (closeIndex == -1) break;

            String tag = text.substring(openIndex + 1, closeIndex);
            String tagName = tag.split(" ")[0];

            if (tagName.charAt(0) == '/') {
                content.append(text, index, openIndex);
                var pop = stack.pop();
                if (pop.getChildren().size() > 0 && isContentNotEmpty(content)) {
                    throw new RuntimeException("Cannot have content and children");
                }
                if (isContentNotEmpty(content)) {
                    pop.setContent(formatContent(content.toString()));
                    content = new StringBuilder();
                }
                stack.peek().addChild(pop);
                index = closeIndex + 1;
                continue;
            } else {
                content.append(text, index, openIndex);
                if (isContentNotEmpty(content)) {
                    System.out.println(content);
                    throw new RuntimeException("Cannot have content before a tag");
                }
                Map<String, String> parameters = parseParameters(tag, tagName);
                if (!this.serializers.containsKey(tagName)) {
                    throw new RuntimeException("Unknown tag: " + tagName);
                }
                stack.push(this.serializers.get(tagName).deserialize(parameters));
            }

            index = closeIndex + 1;
        }

        return stack.peek().getChildren();
    }

    private static String formatContent(String content) {
        content = LESS_THAN_REPLACE.matcher(content).replaceAll(result -> result.groupCount() > 0 ? result.group(1) + "<" : "<");
        content = GREATER_THAN_REPLACE.matcher(content).replaceAll(result -> result.groupCount() > 0 ? result.group(1) + ">" : ">");
        return content.trim();
    }

    private static int indexOfUnescaped(char c, String text, int index) {
        while (index < text.length()) {
            if (text.charAt(index) == c) {
                if (index == 0 || text.charAt(index - 1) != '\\') {
                    return index;
                }
            }
            index++;
        }
        return -1;
    }

    private static boolean isContentNotEmpty(StringBuilder builder) {
        for (int i = 0; i < builder.length(); i++) {
            if (!Character.isWhitespace(builder.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    private static Map<String, String> parseParameters(String content, String tag) {
        String parametersText = content.substring(tag.length()).trim();
        Map<String, String> parameters = new HashMap<>();
        String key = null;
        int index = 0;
        while (index < parametersText.length()) {
            if (key == null) {
                if (parametersText.charAt(index) == ' ') {
                    index++;
                    continue;
                }
                int equalsIndex = parametersText.indexOf('=', index);
                if (equalsIndex == -1) break;
                key = parametersText.substring(index, equalsIndex);
                index = equalsIndex + 1;
                continue;
            }
            if (parametersText.charAt(index) == '"') {
                int endIndex = parametersText.indexOf('"', index + 1);
                if (endIndex == -1) break;
                parameters.put(key, parametersText.substring(index + 1, endIndex));
                key = null;
                index = endIndex + 1;
            }
        }
        return parameters;
    }

    private record Root(List<TagElement> children) implements TagElement {

        @Override
        public void addChild(TagElement element) {
            children.add(element);
        }

        @Override
        public @NotNull List<TagElement> getChildren() {
            return children;
        }
    }
}
