package dev.siea.jonion.configuration.finder;

import dev.siea.jonion.configuration.XmlPluginConfig;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Finds and loads plugin configuration from XML files.
 * <p>
 * Resolves config under {@code <parent of path>/<pluginId>/<configFileName>} on the filesystem,
 * or from the root of the JAR at {@code path} if the file is not on disk. Default file name
 * is {@code config.xml}. Returns an {@link XmlPluginConfig} backed by a W3C DOM document.
 * </p>
 *
 * @see PluginConfigurationFinder
 * @see dev.siea.jonion.configuration.XmlPluginConfig
 */
public class XmlConfigurationFinder implements PluginConfigurationFinder {
    private final String configFileName;

    /** Creates a finder that looks for {@code config.xml}. */
    public XmlConfigurationFinder() {
        this("config.xml");
    }

    /**
     * Creates a finder that looks for the given config file name.
     *
     * @param configFileName the default config file name (e.g. {@code config.xml})
     */
    public XmlConfigurationFinder(String configFileName) {
        this.configFileName = configFileName;
    }

    @Override
    public XmlPluginConfig findPluginConfiguration(String pluginId, Path path) {
        return findPluginConfiguration(pluginId, path, configFileName);
    }

    @Override
    public XmlPluginConfig findPluginConfiguration(String pluginId, Path path, String configFileName) {
        Path filePath = path.getParent().resolve(pluginId + "/" + configFileName);

        Document document;

        if (Files.exists(filePath)) {
            try (InputStream inputStream = Files.newInputStream(filePath)) {
                document = loadDocument(inputStream);
            } catch (IOException e) {
                return null;
            }
        } else {
            try (JarFile jarFile = new JarFile(path.toFile())) {
                JarEntry entry = jarFile.getJarEntry(configFileName);
                if (entry != null) {
                    try (InputStream inputStream = jarFile.getInputStream(entry)) {
                        document = loadDocument(inputStream);
                    }
                } else {
                    document = createEmptyDocument();
                }
            } catch (IOException ignored) {
                return null;
            }
        }

        if (document == null) {
            return null;
        }

        return new XmlPluginConfig(document, filePath);
    }

    private Document loadDocument(InputStream inputStream) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(inputStream);
        } catch (Exception e) {
            return null;
        }
    }

    private Document createEmptyDocument() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            document.appendChild(document.createElement("config"));
            return document;
        } catch (Exception e) {
            return null;
        }
    }
}
