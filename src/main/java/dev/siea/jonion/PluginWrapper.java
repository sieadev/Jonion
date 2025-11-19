package dev.siea.jonion;

import dev.siea.jonion.configuration.finder.PluginConfigurationFinder;
import dev.siea.jonion.descriptor.PluginDescriptor;
import dev.siea.jonion.exceptions.PluginLoadException;
import dev.siea.jonion.lifecycle.PluginState;
import dev.siea.jonion.manager.PluginManager;
import dev.siea.jonion.loader.CustomClassLoader;

import java.nio.file.Path;

public class PluginWrapper {
    private final PluginDescriptor pluginDescriptor;
    private final Path path;
    private final PluginManager pluginManager;
    private final PluginConfigurationFinder configurationFinder;
    private Plugin plugin;
    private CustomClassLoader classLoader;
    private PluginState state = PluginState.CREATED;

    public PluginWrapper(PluginManager pluginManager, PluginDescriptor pluginDescriptor, PluginConfigurationFinder configurationFinder, Path path) {
        this.pluginManager = pluginManager;
        this.pluginDescriptor = pluginDescriptor;
        this.configurationFinder = configurationFinder;
        this.path = path;
    }

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

    public void unload() {
        plugin = null;
        classLoader = null;
        state = PluginState.UNLOADED;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public PluginDescriptor getPluginDescriptor() {
        return pluginDescriptor;
    }

    public PluginState getState() {
        return state;
    }

    public void setState(PluginState state) {
        this.state = state;
    }

    public PluginManager getPluginManager() {
        return pluginManager;
    }

    public Path getPath() {
        return path;
    }
}