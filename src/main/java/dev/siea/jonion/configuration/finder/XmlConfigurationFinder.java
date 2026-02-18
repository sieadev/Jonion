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

public class XmlConfigurationFinder implements PluginConfigurationFinder {
    private final String configFileName;

    public XmlConfigurationFinder() {
        this("config.xml");
    }

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
