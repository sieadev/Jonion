package dev.siea.jonion;

import dev.siea.jonion.configuration.finder.PluginConfigurationFinder;
import dev.siea.jonion.descriptor.PluginDescriptor;
import dev.siea.jonion.exceptions.PluginLoadException;
import dev.siea.jonion.lifecycle.PluginState;
import dev.siea.jonion.loader.CustomClassLoader;
import dev.siea.jonion.manager.PluginManager;

import java.nio.file.Path;

/**
 * Wraps a single plugin with its descriptor, path, class loader, and lifecycle state.
 * <p>
 * Created by the {@link dev.siea.jonion.manager.AbstractPluginManager} for each
 * discovered plugin JAR. The wrapper holds the {@link Plugin} instance after
 * {@link #load()} is called and clears it on {@link #unload()}. It provides
 * access to the plugin's metadata ({@link PluginDescriptor}), file path,
 * current {@link PluginState}, and the managing {@link PluginManager}.
 * </p>
 *
 * @see Plugin
 * @see PluginDescriptor
 * @see PluginState
 * @see dev.siea.jonion.manager.PluginManager
 */
public class PluginWrapper {
    private final PluginDescriptor pluginDescriptor;
    private final Path path;
    private final PluginManager pluginManager;
    private final PluginConfigurationFinder configurationFinder;
    private Plugin plugin;
    private CustomClassLoader classLoader;
    private PluginState state = PluginState.CREATED;

    /**
     * Creates a wrapper for a plugin. The plugin is not loaded until {@link #load()} is called.
     *
     * @param pluginManager         the manager that owns this wrapper
     * @param pluginDescriptor     the plugin metadata
     * @param configurationFinder  the finder for plugin configuration files
     * @param path                  the path to the plugin JAR
     */
    public PluginWrapper(PluginManager pluginManager, PluginDescriptor pluginDescriptor, PluginConfigurationFinder configurationFinder, Path path) {
        this.pluginManager = pluginManager;
        this.pluginDescriptor = pluginDescriptor;
        this.configurationFinder = configurationFinder;
        this.path = path;
    }

    /**
     * Loads the plugin: creates a class loader from the JAR path, instantiates the plugin
     * via {@link PluginFactory}, and sets state to {@link PluginState#LOADED}.
     * On failure, state is set to {@link PluginState#FAILED}.
     *
     * @throws PluginLoadException if the plugin cannot be loaded or instantiated
     */
    public void load() throws PluginLoadException {
        try {
            this.classLoader = new CustomClassLoader(path, PluginWrapper.class.getClassLoader());
            this.plugin = PluginFactory.createPlugin(pluginDescriptor, classLoader);
            plugin.load(this, pluginDescriptor, configurationFinder);
            state = PluginState.LOADED;
        } catch (Throwable e) {
            state = PluginState.FAILED;
            throw new PluginLoadException(e.getMessage(), e);
        }
    }

    /** Unloads the plugin by clearing the instance and class loader and setting state to {@link PluginState#UNLOADED}. */
    public void unload() {
        plugin = null;
        classLoader = null;
        state = PluginState.UNLOADED;
    }

    /**
     * Returns the loaded plugin instance. Null before {@link #load()} or after {@link #unload()}.
     *
     * @return the plugin instance, or null if not loaded
     */
    public Plugin getPlugin() {
        return plugin;
    }

    /** Returns the plugin metadata (ID, version, class name, dependencies, etc.). */
    public PluginDescriptor getPluginDescriptor() {
        return pluginDescriptor;
    }

    /** Returns the current lifecycle state of this plugin. */
    public PluginState getState() {
        return state;
    }

    /**
     * Sets the lifecycle state (e.g. to {@link PluginState#FAILED} when dependency resolution fails).
     *
     * @param state the new state
     */
    public void setState(PluginState state) {
        this.state = state;
    }

    /** Returns the plugin manager that owns this wrapper. */
    public PluginManager getPluginManager() {
        return pluginManager;
    }

    /** Returns the path to the plugin JAR file. */
    public Path getPath() {
        return path;
    }
}