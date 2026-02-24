package dev.siea.jonion.dependency;

/**
 * Declares a dependency on another plugin by ID and whether it is optional.
 * <p>
 * Used in {@link dev.siea.jonion.descriptor.PluginDescriptor#getDependencies()} and by
 * {@link dev.siea.jonion.manager.AbstractPluginManager} to sort load order and detect
 * missing or circular dependencies. Required dependencies must be loaded first; optional
 * ones are skipped if absent.
 * </p>
 *
 * @see dev.siea.jonion.descriptor.PluginDescriptor
 * @see dev.siea.jonion.manager.AbstractPluginManager
 */
public class PluginDependency {
    private final String pluginId;
    private final boolean optional;

    /**
     * Creates a dependency on the given plugin.
     *
     * @param pluginId the unique ID of the required plugin
     * @param optional true if the plugin may be absent (load can continue); false if it must be present
     */
    public PluginDependency(String pluginId, boolean optional) {
        this.pluginId = pluginId;
        this.optional = optional;
    }

    /** Returns the plugin ID this dependency refers to. */
    public String getPluginId() {
        return this.pluginId;
    }

    /** Returns whether this dependency is optional (may be missing). */
    public boolean isOptional() {
        return this.optional;
    }

    @Override
    public boolean equals(Object dependency) {
        if (this == dependency) {
            return true;
        } else if (!(dependency instanceof PluginDependency that)) {
            return false;
        } else {
            return this.optional == that.optional && this.pluginId.equals(that.pluginId);
        }
    }
}
