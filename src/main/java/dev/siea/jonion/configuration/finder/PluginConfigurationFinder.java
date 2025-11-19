package dev.siea.jonion.configuration.finder;

import dev.siea.jonion.configuration.PluginConfig;

import java.nio.file.Path;

public interface PluginConfigurationFinder {
    PluginConfig findPluginConfiguration(String pluginId, Path path);
    PluginConfig findPluginConfiguration(String pluginId, Path path, String configFileName);
}
