package dev.siea.jonion.configuration.finder;

import dev.siea.jonion.configuration.PluginConfig;

import java.nio.file.Path;

/**
 * Strategy for locating and loading a plugin configuration file given a plugin ID and path.
 * <p>
 * Implementations resolve a config file (e.g. from the filesystem next to the JAR or from
 * inside the JAR) and return a {@link PluginConfig}. Used by
 * {@link dev.siea.jonion.Plugin#getDefaultConfig()} and {@link dev.siea.jonion.Plugin#getConfig(String)}.
 * </p>
 *
 * @see PluginConfig
 * @see YamlConfigurationFinder
 * @see XmlConfigurationFinder
 * @see dev.siea.jonion.Plugin
 */
public interface PluginConfigurationFinder {
    /**
     * Finds the default configuration for the plugin (convention-based file name).
     *
     * @param pluginId the plugin identifier (used for path resolution)
     * @param path     path to the plugin JAR or resource root
     * @return the loaded config, or null if not found or load fails
     */
    PluginConfig findPluginConfiguration(String pluginId, Path path);

    /**
     * Finds a named configuration file for the plugin.
     *
     * @param pluginId       the plugin identifier (used for path resolution)
     * @param path           path to the plugin JAR or resource root
     * @param configFileName the config file name (e.g. {@code config.yml}, {@code messages.yml})
     * @return the loaded config, or null if not found or load fails
     */
    PluginConfig findPluginConfiguration(String pluginId, Path path, String configFileName);
}
