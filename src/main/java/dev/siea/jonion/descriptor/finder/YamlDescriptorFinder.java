package dev.siea.jonion.descriptor.finder;

import dev.siea.jonion.dependency.PluginDependency;
import dev.siea.jonion.descriptor.DefaultPluginDescriptor;
import dev.siea.jonion.descriptor.PluginDescriptor;
import org.simpleyaml.configuration.ConfigurationSection;
import org.simpleyaml.configuration.file.YamlConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class YamlDescriptorFinder implements PluginDescriptorFinder {
    private static final Logger log = LoggerFactory.getLogger(YamlDescriptorFinder.class);
    private final String descriptorFileName;

    public YamlDescriptorFinder() {
        this("plugin.yml");
    }

    public YamlDescriptorFinder(String descriptorFileName) {
        this.descriptorFileName = descriptorFileName;
    }

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
        } catch (IOException e) {
            log.debug("Could not read plugin descriptor from {}: {}", path, e.getMessage(), e);
            return null;
        }
        String pluginId = yamlConfig.getString("name");
        String description = yamlConfig.getString("description", "");
        String version = yamlConfig.getString("version", "UNDEFINED");
        String pluginClass = yamlConfig.getString("main");
        List<String> authors = yamlConfig.getStringList("authors");
        if (authors == null) {
            authors = Collections.emptyList();
        }
        String license = yamlConfig.getString("license", "UNDEFINED");
        DefaultPluginDescriptor descriptor = new DefaultPluginDescriptor(pluginId, description, version, pluginClass, authors, license);

        ConfigurationSection configurationSection = yamlConfig.getConfigurationSection("dependencies");
        if (configurationSection != null) {
            try {
                for (String dependency : configurationSection.getKeys(false)) {
                    descriptor.addDependency(new PluginDependency(dependency, yamlConfig.getBoolean("dependencies." + dependency)));
                }
            } catch (Exception e) {
                log.debug("Could not parse dependencies section in descriptor: {}", e.getMessage(), e);
            }
        }

        return descriptor;
    }
}
