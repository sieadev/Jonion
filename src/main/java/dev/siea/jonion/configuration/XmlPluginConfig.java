package dev.siea.jonion.configuration;

import dev.siea.jonion.exceptions.ConfigSaveException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.nio.file.Path;

public class XmlPluginConfig extends PluginConfig {
    private static final String ROOT_TAG = "config";

    private final Document document;
    private final File file;

    public XmlPluginConfig(Document document, Path filePath) {
        this.document = document;
        this.file = filePath != null ? new File(filePath.toString()) : null;
    }

    public Document getDocument() {
        return document;
    }

    private Element getOrCreateElement(String path) {
        String[] parts = path.split("\\.");
        Element current = document.getDocumentElement();
        if (current == null) {
            current = document.createElement(ROOT_TAG);
            document.appendChild(current);
        }
        for (String part : parts) {
            NodeList children = current.getChildNodes();
            Element child = null;
            for (int i = 0; i < children.getLength(); i++) {
                Node node = children.item(i);
                if (node instanceof Element && part.equals(((Element) node).getTagName())) {
                    child = (Element) node;
                    break;
                }
            }
            if (child == null) {
                child = document.createElement(part);
                current.appendChild(child);
            }
            current = child;
        }
        return current;
    }

    private Element getElement(String path) {
        String[] parts = path.split("\\.");
        Element current = document.getDocumentElement();
        if (current == null) {
            return null;
        }
        for (String part : parts) {
            NodeList children = current.getChildNodes();
            Element found = null;
            for (int i = 0; i < children.getLength(); i++) {
                Node node = children.item(i);
                if (node instanceof Element && part.equals(((Element) node).getTagName())) {
                    found = (Element) node;
                    break;
                }
            }
            current = found;
            if (current == null) {
                return null;
            }
        }
        return current;
    }

    private String getTextContent(Element element) {
        if (element == null) {
            return null;
        }
        String text = element.getTextContent();
        return text != null ? text.trim() : null;
    }

    @Override
    public boolean containsKey(String path) {
        return getElement(path) != null;
    }

    @Override
    public String getString(String path) {
        Element element = getElement(path);
        return getTextContent(element);
    }

    @Override
    public int getInt(String path) {
        String value = getString(path);
        if (value == null || value.isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @Override
    public boolean getBoolean(String path) {
        String value = getString(path);
        if (value == null || value.isEmpty()) {
            return false;
        }
        return Boolean.parseBoolean(value);
    }

    @Override
    public double getDouble(String path) {
        String value = getString(path);
        if (value == null || value.isEmpty()) {
            return 0.0;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    @Override
    public long getLong(String path) {
        String value = getString(path);
        if (value == null || value.isEmpty()) {
            return 0L;
        }
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return 0L;
        }
    }

    @Override
    public float getFloat(String path) {
        return (float) getDouble(path);
    }

    @Override
    public byte getByte(String path) {
        return (byte) getInt(path);
    }

    @Override
    public short getShort(String path) {
        return (short) getInt(path);
    }

    @Override
    public char getChar(String path) {
        String value = getString(path);
        if (value == null || value.isEmpty()) {
            return '\u0000';
        }
        return value.charAt(0);
    }

    @Override
    public Object get(String path) {
        return getString(path);
    }

    @Override
    public void set(String path, Object value) {
        Element element = getOrCreateElement(path);
        element.setTextContent(value != null ? value.toString() : "");
    }

    @Override
    public void save() {
        if (file == null) {
            return;
        }
        try {
            if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
                throw new ConfigSaveException("Failed to create directories for the configuration file.");
            }
            if (!file.exists() && !file.createNewFile()) {
                throw new ConfigSaveException("Failed to create the configuration file.");
            }
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(new DOMSource(document), new StreamResult(file));
        } catch (Exception e) {
            throw new ConfigSaveException(e);
        }
    }
}
