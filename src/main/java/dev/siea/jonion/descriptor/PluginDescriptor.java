package dev.siea.jonion.descriptor;

import dev.siea.jonion.dependency.PluginDependency;

import java.util.List;

/**
 * Metadata for a plugin: identity, version, main class, authors, license, and dependencies.
 * <p>
 * Descriptors are read from plugin JARs by a {@link dev.siea.jonion.descriptor.finder.PluginDescriptorFinder}
 * (e.g. from {@code plugin.yml} or {@code plugin.xml}) and used by the plugin manager to load and
 * order plugins.
 * </p>
 *
 * @see DefaultPluginDescriptor
 * @see dev.siea.jonion.descriptor.finder.PluginDescriptorFinder
 * @see dev.siea.jonion.depedency.PluginDependency
 */
public interface PluginDescriptor {
    /** Returns the unique plugin identifier. */
    String getPluginId();

    /** Returns a short description of the plugin. */
    String getDescription();

    /** Returns the plugin version string. */
    String getVersion();

    /** Returns the fully qualified name of the plugin main class (extends {@link dev.siea.jonion.Plugin}). */
    String getPluginClass();

    /** Returns the list of author names. */
    List<String> getAuthors();

    /** Returns the license identifier or name. */
    String getLicense();

    /** Returns the list of plugin dependencies (required and optional). */
    List<PluginDependency> getDependencies();
}
