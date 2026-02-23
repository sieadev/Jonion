package dev.siea.jonion.descriptor.finder;

import dev.siea.jonion.dependency.PluginDependency;
import dev.siea.jonion.descriptor.DefaultPluginDescriptor;
import dev.siea.jonion.descriptor.PluginDescriptor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Finds and parses a plugin descriptor from an XML file inside the plugin JAR.
 * <p>
 * Looks for a file (default {@code plugin.xml}) at the JAR root. Expected structure includes
 * {@code name}, {@code main}, {@code version}, {@code description}, {@code authors}
 * (with {@code author} children), {@code license}, and optionally {@code dependencies}
 * with {@code dependency} elements (attributes {@code id} and {@code optional}).
 * </p>
 *
 * @see PluginDescriptorFinder
 * @see PluginDescriptor
 */
public class XmlDescriptorFinder implements PluginDescriptorFinder {
    private final String descriptorFileName;

    /** Creates a finder that looks for {@code plugin.xml} in the JAR. */
    public XmlDescriptorFinder() {
        this("plugin.xml");
    }

    /**
     * Creates a finder that looks for the given descriptor file name in the JAR.
     *
     * @param descriptorFileName the name of the XML file (e.g. {@code plugin.xml})
     */
    public XmlDescriptorFinder(String descriptorFileName) {
        this.descriptorFileName = descriptorFileName;
    }

    @Override
    public PluginDescriptor findPluginDescriptor(Path path) {
        Document document;
        try (JarFile jarFile = new JarFile(path.toFile())) {
            JarEntry entry = jarFile.getJarEntry(descriptorFileName);
            if (entry != null) {
                try (InputStream inputStream = jarFile.getInputStream(entry)) {
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    document = builder.parse(inputStream);
                }
            } else {
                return null;
            }
        } catch (Exception ignored) {
            return null;
        }

        Element root = document.getDocumentElement();
        if (root == null) {
            return null;
        }

        String pluginId = getTextContent(root, "name");
        if (pluginId == null || pluginId.isEmpty()) {
            return null;
        }

        String description = getTextContent(root, "description");
        if (description == null) {
            description = "";
        }

        String version = getTextContent(root, "version");
        if (version == null) {
            version = "UNDEFINED";
        }

        String pluginClass = getTextContent(root, "main");
        if (pluginClass == null || pluginClass.isEmpty()) {
            return null;
        }

        List<String> authors = getListContent(root);
        String license = getTextContent(root, "license");
        if (license == null) {
            license = "UNDEFINED";
        }

        DefaultPluginDescriptor descriptor = new DefaultPluginDescriptor(
                pluginId, description, version, pluginClass, authors, license);

        Element dependenciesElement = getFirstChildElement(root, "dependencies");
        if (dependenciesElement != null) {
            NodeList dependencyNodes = dependenciesElement.getElementsByTagName("dependency");
            for (int i = 0; i < dependencyNodes.getLength(); i++) {
                Node node = dependencyNodes.item(i);
                if (node instanceof Element dep) {
                    String id = dep.getAttribute("id");
                    if (!id.isEmpty()) {
                        boolean optional = Boolean.parseBoolean(dep.getAttribute("optional"));
                        descriptor.addDependency(new PluginDependency(id, optional));
                    }
                }
            }
        }

        return descriptor;
    }

    private String getTextContent(Element parent, String tagName) {
        Element element = getFirstChildElement(parent, tagName);
        if (element == null) {
            return null;
        }
        String text = element.getTextContent();
        return text != null ? text.trim() : null;
    }

    private List<String> getListContent(Element parent) {
        List<String> result = new ArrayList<>();
        Element container = getFirstChildElement(parent, "authors");
        if (container == null) {
            return result;
        }
        NodeList children = container.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if (node instanceof Element && "author".equals(((Element) node).getTagName())) {
                String text = node.getTextContent();
                if (text != null && !text.trim().isEmpty()) {
                    result.add(text.trim());
                }
            }
        }
        return result;
    }

    private Element getFirstChildElement(Element parent, String tagName) {
        NodeList children = parent.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if (node instanceof Element && tagName.equals(((Element) node).getTagName())) {
                return (Element) node;
            }
        }
        return null;
    }
}
