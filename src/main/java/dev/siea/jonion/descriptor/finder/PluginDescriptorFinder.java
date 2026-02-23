package dev.siea.jonion.descriptor.finder;

import dev.siea.jonion.descriptor.PluginDescriptor;

import java.nio.file.Path;

/**
 * Strategy for locating and parsing a plugin descriptor from a JAR path.
 * <p>
 * Implementations read a descriptor file (e.g. YAML or XML) from inside the JAR and return a
 * {@link PluginDescriptor}, or {@code null} if no descriptor is found or parsing fails.
 * Used by {@link dev.siea.jonion.manager.AbstractPluginManager} when discovering plugins.
 * </p>
 *
 * @see PluginDescriptor
 * @see YamlDescriptorFinder
 * @see XmlDescriptorFinder
 * @see dev.siea.jonion.manager.AbstractPluginManager
 */
public interface PluginDescriptorFinder {
    /**
     * Attempts to find and parse a plugin descriptor from the given JAR path.
     *
     * @param path path to the plugin JAR file
     * @return the parsed descriptor, or {@code null} if not found or invalid
     */
    PluginDescriptor findPluginDescriptor(Path path);
}
