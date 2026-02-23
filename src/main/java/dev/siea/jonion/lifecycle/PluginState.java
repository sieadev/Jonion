package dev.siea.jonion.lifecycle;

/**
 * Lifecycle state of a plugin as managed by the plugin system.
 * <p>
 * Used by {@link dev.siea.jonion.PluginWrapper} and
 * {@link dev.siea.jonion.manager.PluginManager} to track whether a plugin has been
 * created, successfully loaded, failed during load, or unloaded.
 * </p>
 *
 * @see dev.siea.jonion.PluginWrapper#getState()
 * @see dev.siea.jonion.manager.PluginManager#getPlugins(PluginState)
 */
public enum PluginState {
    /** Wrapper created from descriptor; plugin not yet loaded. */
    CREATED,

    /** Plugin instance loaded and ready to use. */
    LOADED,

    /** Load or startup failed; plugin is not active. */
    FAILED,

    /** Plugin was unloaded; instance and class loader cleared. */
    UNLOADED
}
