package earth.terrarium.hermes.api;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TagProvider {

    private final Map<String, TagElementSerializer> serializers = new HashMap<>();

    public void addSerializer(String tag, TagElementSerializer serializer) {
        this.serializers.put(tag, serializer);
    }

    public List<TagElement> parse(String text) {
        try {
            InputStream stream = new ByteArrayInputStream(createRoot(text).getBytes(StandardCharsets.UTF_8));
            InputSource in = new InputSource(stream);
            in.setEncoding("UTF-8");
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in);
            Node root = document.getDocumentElement();
            root.normalize();
            return nodeToElements(root);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new TagParseException("Failed to parse tag text", e);
        }
    }

    private static String createRoot(String input) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root>" + input + "</root>";
    }

    private List<TagElement> nodeToElements(Node node) {
        List<TagElement> elements = new ArrayList<>();
        for (int i = 0; i < node.getChildNodes().getLength(); i++) {
            Node child = node.getChildNodes().item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                TagElementSerializer serializer = serializers.get(child.getNodeName());
                if (serializer != null) {
                    TagElement element = serializer.deserialize(mapAttributes(child.getAttributes()));
                    if (element != null) {
                        elements.add(element);
                        if (hasChildOf(child, Node.ELEMENT_NODE)) {
                            element.getChildTagProvider(this)
                                .nodeToElements(child)
                                .forEach(element::addChild);
                        } else {
                            String text = child.getTextContent();
                            if (StringUtils.isNotBlank(text)) {
                                element.addText(text);
                            }
                        }
                    }
                } else {
                    throw new TagParseException("Unknown tag: " + child.getNodeName());
                }
            }
        }
        return elements;
    }

    private boolean hasChildOf(Node node, short type) {
        for (int i = 0; i < node.getChildNodes().getLength(); i++) {
            Node child = node.getChildNodes().item(i);
            if (child.getNodeType() == type && !child.getTextContent().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private Map<String, String> mapAttributes(NamedNodeMap map) {
        Map<String, String> attributes = new HashMap<>();
        for (int i = 0; i < map.getLength(); i++) {
            Node attribute = map.item(i);
            attributes.put(attribute.getNodeName(), attribute.getNodeValue());
        }
        return attributes;
    }
}
