package dev.siea.jonion;

import dev.siea.jonion.descriptor.PluginDescriptor;
import dev.siea.jonion.exceptions.PluginLoadException;
import dev.siea.jonion.loader.CustomClassLoader;

/**
 * Factory for creating {@link Plugin} instances from a descriptor and class loader.
 * <p>
 * Loads the plugin main class (from {@link PluginDescriptor#getPluginClass()})
 * using the given {@link CustomClassLoader}, instantiates it, and returns it as
 * a {@link Plugin}. The class loader ensures the plugin runs in an isolated
 * class-loading context based on its JAR path.
 * </p>
 *
 * @see Plugin
 * @see PluginDescriptor
 * @see CustomClassLoader
 * @see PluginLoadException
 */
public class PluginFactory {
    /**
     * Creates a new plugin instance from the descriptor using the provided class loader.
     *
     * @param descriptor   the plugin descriptor containing the main class name
     * @param classLoader  the class loader to use to load the plugin class (typically from its JAR)
     * @return a new instance of the plugin main class
     * @throws PluginLoadException if the class cannot be loaded or instantiated
     */
    public static Plugin createPlugin(PluginDescriptor descriptor, CustomClassLoader classLoader) throws PluginLoadException {
        try {
            Class<?> pluginClass = classLoader.loadClass(descriptor.getPluginClass());
            return (Plugin) pluginClass.newInstance();
        } catch (Throwable e) {
            throw new PluginLoadException("Failed to create plugin", e);
        }
    }
}