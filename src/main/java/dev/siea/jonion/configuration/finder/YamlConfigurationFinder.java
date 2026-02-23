package dev.siea.jonion.configuration.finder;

import dev.siea.jonion.configuration.YamlPluginConfig;
import org.simpleyaml.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Finds and loads plugin configuration from YAML files.
 * <p>
 * Resolves config under {@code <parent of path>/<pluginId>/<configFileName>} on the filesystem,
 * or from the root of the JAR at {@code path} if the file is not on disk. Default file name
 * is {@code config.yml}. Returns a {@link YamlPluginConfig} backed by Simple-YAML.
 * </p>
 *
 * @see PluginConfigurationFinder
 * @see dev.siea.jonion.configuration.YamlPluginConfig
 */
public class YamlConfigurationFinder implements PluginConfigurationFinder {
    private final String configFileName;

    /** Creates a finder that looks for {@code config.yml}. */
    public YamlConfigurationFinder() {
        this("config.yml");
    }

    /**
     * Creates a finder that looks for the given config file name.
     *
     * @param configFileName the default config file name (e.g. {@code config.yml})
     */
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
            } catch (IOException ignored) {
                return null;
            }
        }

        return new YamlPluginConfig(yamlConfig, filePath);
    }
}
