package dev.siea.jonion.configuration.finder;

import dev.siea.jonion.configuration.XmlPluginConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(XmlConfigurationFinder.class);
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
                log.debug("Could not load config from {}: {}", filePath, e.getMessage(), e);
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
            } catch (IOException e) {
                log.debug("Could not load config from JAR {}: {}", path, e.getMessage(), e);
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
            log.debug("Could not parse XML config: {}", e.getMessage(), e);
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
            log.debug("Could not create empty XML document: {}", e.getMessage(), e);
            return null;
        }
    }
}
