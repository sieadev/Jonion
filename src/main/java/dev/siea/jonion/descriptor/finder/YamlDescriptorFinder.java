package dev.siea.jonion.descriptor.finder;

import dev.siea.jonion.dependency.PluginDependency;
import dev.siea.jonion.descriptor.DefaultPluginDescriptor;
import dev.siea.jonion.descriptor.PluginDescriptor;
import org.simpleyaml.configuration.ConfigurationSection;
import org.simpleyaml.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Finds and parses a plugin descriptor from a YAML file inside the plugin JAR.
 * <p>
 * Looks for a file (default {@code plugin.yml}) at the JAR root. Expected keys include
 * {@code name}, {@code main}, {@code version}, {@code description}, {@code authors},
 * {@code license}, and optionally {@code dependencies} (map of plugin ID to optional boolean).
 * </p>
 *
 * @see PluginDescriptorFinder
 * @see PluginDescriptor
 */
public class YamlDescriptorFinder implements PluginDescriptorFinder {
    private final String descriptorFileName;

    /** Creates a finder that looks for {@code plugin.yml} in the JAR. */
    public YamlDescriptorFinder() {
        this("plugin.yml");
    }

    /**
     * Creates a finder that looks for the given descriptor file name in the JAR.
     *
     * @param descriptorFileName the name of the YAML file (e.g. {@code plugin.yml})
     */
    public YamlDescriptorFinder(String descriptorFileName) {
        this.descriptorFileName = descriptorFileName;
    }

    @Override
    public PluginDescriptor findPluginDescriptor(Path path) {
        YamlConfiguration yamlConfig = new YamlConfiguration();
        try (JarFile jarFile = new JarFile(path.toFile())) {
            JarEntry entry = jarFile.getJarEntry(descriptorFileName);
            if (entry != null) {
                try (InputStream inputStream = jarFile.getInputStream(entry)) {
                    yamlConfig.load(inputStream);
                }
            } else {
                return null;
            }
        } catch (IOException ignored) {
            return null;
        }
        String pluginId = yamlConfig.getString("name");
        String description = yamlConfig.getString("description", "");
        String version = yamlConfig.getString("version", "UNDEFINED");
        String pluginClass = yamlConfig.getString("main");
        List<String> authors = yamlConfig.getStringList("authors");
        String license = yamlConfig.getString("license", "UNDEFINED");
        DefaultPluginDescriptor descriptor = new DefaultPluginDescriptor(pluginId, description, version, pluginClass, authors, license);

        ConfigurationSection configurationSection = yamlConfig.getConfigurationSection("dependencies");
        if (configurationSection != null) {
            try{
                for (String dependency : configurationSection.getKeys(false)) {
                    descriptor.addDependency(new PluginDependency(dependency, yamlConfig.getBoolean("dependencies." + dependency)));
                }
            } catch (Exception ignored) {}
        }

        return descriptor;
    }
}
