package dev.siea.jonion.manager;

import dev.siea.jonion.PluginWrapper;
import dev.siea.jonion.lifecycle.PluginState;

import java.util.List;

/**
 * Manages the lifecycle and registry of plugins.
 * <p>
 * Implementations discover plugins (typically from a directory of JARs), create
 * {@link PluginWrapper} instances, and load them in dependency order. This
 * interface exposes read-only access to the current set of plugins and lookup
 * by ID or state.
 * </p>
 *
 * @see PluginWrapper
 * @see PluginState
 */
public interface PluginManager {
    /**
     * Returns all plugins known to this manager, in load order (dependencies first).
     *
     * @return a non-null list of plugin wrappers; may be empty
     */
    List<PluginWrapper> getPlugins();

    /**
     * Returns plugins that are in the given lifecycle state.
     *
     * @param state the state to filter by (e.g. {@link PluginState#LOADED})
     * @return a non-null list of matching plugin wrappers; may be empty
     */
    List<PluginWrapper> getPlugins(PluginState state);

    /**
     * Looks up a plugin by its unique identifier.
     *
     * @param pluginId the plugin ID from its descriptor
     * @return the wrapper for that plugin, or {@code null} if not found
     */
    PluginWrapper getPlugin(String pluginId);
}
