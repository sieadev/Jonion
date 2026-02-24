package dev.siea.jonion.manager;

import dev.siea.jonion.descriptor.finder.PluginDescriptorFinder;
import dev.siea.jonion.descriptor.finder.YamlDescriptorFinder;
import dev.siea.jonion.impl.SimplePlugin;
import dev.siea.jonion.lifecycle.PluginState;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Default implementation of {@link AbstractPluginManager} that adds start/stop lifecycle
 * support for {@link SimplePlugin} instances.
 * <p>
 * After plugins are loaded (by the parent class), {@link #start()} invokes
 * {@link SimplePlugin#start()} on each loaded plugin. {@link #stop()} invokes
 * {@link SimplePlugin#stop()} on each loaded plugin and then {@link #unloadPlugins() unloads}
 * all plugins. Only wrappers in {@link PluginState#LOADED} state are started or stopped;
 * failures are logged and the wrapper's state is set to {@link PluginState#FAILED}.
 * </p>
 * <p>
 * <strong>Note:</strong> This manager assumes all loaded plugins are instances of
 * {@link SimplePlugin}. Plugins that extend {@link dev.siea.jonion.Plugin} but not
 * {@link SimplePlugin} will throw {@link ClassCastException} when started or stopped.
 * </p>
 *
 * @see AbstractPluginManager
 * @see SimplePlugin
 * @see PluginState
 */
public class DefaultPluginManager extends AbstractPluginManager {
    /**
     * Creates a manager that scans the default {@code plugins} directory with the
     * default YAML descriptor finder.
     */
    public DefaultPluginManager() {
        this(Paths.get("plugins"));
    }

    /**
     * Creates a manager that scans the given directory with the default YAML descriptor finder.
     *
     * @param directory the path to the plugin directory (created if it does not exist)
     */
    public DefaultPluginManager(Path directory) {
        this(directory, new YamlDescriptorFinder());
    }

    /**
     * Creates a manager with a custom descriptor finder and default YAML configuration finder.
     *
     * @param directory        the path to the plugin directory (created if it does not exist)
     * @param descriptorFinder the finder used to read plugin descriptors from JARs
     */
    public DefaultPluginManager(Path directory, PluginDescriptorFinder descriptorFinder) {
        super(directory, descriptorFinder);
    }

    /**
     * Starts all loaded plugins by calling {@link SimplePlugin#start()} on each.
     * Only wrappers in {@link PluginState#LOADED} are started. On failure the wrapper
     * is set to {@link PluginState#FAILED} and the error is logged. Logs a summary
     * of how many plugins started successfully and how many failed.
     */
    public void start() {
        AtomicInteger failedCount = new AtomicInteger();
        long toStart = getPlugins().stream().filter(p -> p.getState() == PluginState.LOADED).count();

        getPlugins().forEach(pluginWrapper -> {
            try {
                if (pluginWrapper.getState().equals(PluginState.LOADED)) {
                    ((SimplePlugin) pluginWrapper.getPlugin()).start();
                }
            } catch (Throwable e) {
                pluginWrapper.setState(PluginState.FAILED);
                logger.error("Failed to start plugin: {}", pluginWrapper.getPluginDescriptor().getPluginId(), e);
                failedCount.getAndIncrement();
            }
        });

        logger.info("Successfully started {} plugins. Failed to start {} plugins.", toStart - failedCount.get(), failedCount.get());
    }

    /**
     * Stops all loaded plugins by calling {@link SimplePlugin#stop()} on each, then
     * {@link #unloadPlugins() unloads} every plugin. Only wrappers in
     * {@link PluginState#LOADED} are stopped; failures are logged and the wrapper
     * is set to {@link PluginState#FAILED}.
     */
    public void stop() {
        getPlugins().forEach(pluginWrapper -> {
            try {
                if (pluginWrapper.getState().equals(PluginState.LOADED)) {
                    ((SimplePlugin) pluginWrapper.getPlugin()).stop();
                }
            } catch (Throwable e){
                pluginWrapper.setState(PluginState.FAILED);
                logger.error("Failed to stop plugin: {}", pluginWrapper.getPluginDescriptor().getPluginId(), e);
            }
        });

        unloadPlugins();
    }
}
