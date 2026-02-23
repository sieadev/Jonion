package dev.siea.jonion;

import dev.siea.jonion.configuration.PluginConfig;
import dev.siea.jonion.configuration.finder.PluginConfigurationFinder;
import dev.siea.jonion.descriptor.PluginDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for all plugins loaded by the plugin system.
 * <p>
 * Concrete plugins extend this class and are instantiated by {@link PluginFactory}
 * using the main class name from the plugin descriptor. After creation, the
 * manager calls {@link #load(PluginWrapper, PluginDescriptor, PluginConfigurationFinder)}
 * to inject the wrapper, descriptor, and configuration finder; subclasses can
 * override that method to perform custom initialization (the method is
 * package-private and should not be overridden for normal use).
 * </p>
 * <p>
 * This class provides access to the owning {@link PluginWrapper}, plugin
 * {@link PluginDescriptor} (metadata), a dedicated {@link Logger}, and
 * configuration via {@link PluginConfig} (default and named configs).
 * </p>
 *
 * @see PluginWrapper
 * @see PluginDescriptor
 * @see PluginFactory
 * @see PluginConfig
 */
public abstract class Plugin {
    private PluginWrapper pluginWrapper;
    private PluginDescriptor pluginDescriptor;
    private PluginConfigurationFinder configurationFinder;
    private Logger logger;

    /**
     * Called by the plugin system to inject the wrapper, descriptor, and configuration finder.
     * Subclasses should not need to override this.
     *
     * @param pluginWrapper         the wrapper that owns this plugin
     * @param pluginDescriptor     the plugin metadata
     * @param configurationFinder  the finder for plugin configuration files
     */
    final void load(PluginWrapper pluginWrapper, PluginDescriptor pluginDescriptor, PluginConfigurationFinder configurationFinder) {
        this.pluginWrapper = pluginWrapper;
        this.pluginDescriptor = pluginDescriptor;
        this.configurationFinder = configurationFinder;
        logger = LoggerFactory.getLogger(pluginDescriptor.getPluginId());
    }

    /**
     * Returns the wrapper that owns this plugin (provides access to manager, path, state).
     *
     * @return the plugin wrapper; never null after load
     */
    public PluginWrapper getPluginWrapper() {
        return pluginWrapper;
    }

    /**
     * Returns the plugin metadata (ID, version, description, dependencies, etc.).
     *
     * @return the plugin descriptor; never null after load
     */
    public final PluginDescriptor getMetaData(){
        return pluginDescriptor;
    }

    /**
     * Returns a logger named with the plugin ID for use within this plugin.
     *
     * @return the plugin's logger; never null after load
     */
    public final Logger getLogger() {
        return logger;
    }

    /**
     * Returns the default configuration for this plugin (convention-based name).
     *
     * @return the default plugin configuration; may create the file if missing
     */
    public final PluginConfig getDefaultConfig() {
        return configurationFinder.findPluginConfiguration(pluginDescriptor.getPluginId(), pluginWrapper.getPath());
    }

    /**
     * Returns a named configuration file for this plugin.
     *
     * @param configFileName the configuration file name (e.g. {@code "messages.yml"})
     * @return the plugin configuration for that file; may create it if missing
     */
    public final PluginConfig getConfig(String configFileName) {
        return configurationFinder.findPluginConfiguration(pluginDescriptor.getPluginId(), pluginWrapper.getPath(), configFileName);
    }
}
