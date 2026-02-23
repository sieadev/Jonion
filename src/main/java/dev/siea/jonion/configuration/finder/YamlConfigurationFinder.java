package dev.siea.jonion.configuration.finder;

import dev.siea.jonion.configuration.YamlPluginConfig;
import org.simpleyaml.configuration.file.YamlConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class YamlConfigurationFinder implements PluginConfigurationFinder {
    private static final Logger log = LoggerFactory.getLogger(YamlConfigurationFinder.class);
    private final String configFileName;

    public YamlConfigurationFinder() {
        this("config.yml");
    }

    public YamlConfigurationFinder(String configFileName) {
        this.configFileName = configFileName;
    }

    @Override
    public YamlPluginConfig findPluginConfiguration(String pluginId, Path path) {
        return findPluginConfiguration(pluginId, path, configFileName);
    }

    @Override
    public YamlPluginConfig findPluginConfiguration(String pluginId, Path path, String configFileName) {
        Path filePath = path.getParent().resolve(pluginId + "/" + configFileName);

        YamlConfiguration yamlConfig = new YamlConfiguration();

        if (Files.exists(filePath)) {
            try (InputStream inputStream = Files.newInputStream(filePath)) {
                yamlConfig.load(inputStream);
            } catch (IOException e) {
                log.debug("Could not load config from {}: {}", filePath, e.getMessage(), e);
                return null;
            }
        } else {
            try (JarFile jarFile = new JarFile(path.toFile())) {
                JarEntry entry = jarFile.getJarEntry(configFileName);
                if (entry != null) {
                    try (InputStream inputStream = jarFile.getInputStream(entry)) {
                        yamlConfig.load(inputStream);
                    }
                } else {
                    yamlConfig.loadFromString("");
                }
            } catch (IOException e) {
                log.debug("Could not load config from JAR {}: {}", path, e.getMessage(), e);
                return null;
            }
        }

        return new YamlPluginConfig(yamlConfig, filePath);
    }
}
