package dev.siea.jonion;

import dev.siea.jonion.descriptor.PluginDescriptor;
import dev.siea.jonion.exceptions.PluginLoadException;
import dev.siea.jonion.loader.CustomClassLoader;

import java.lang.reflect.InvocationTargetException;

public class PluginFactory {
    public static Plugin createPlugin(PluginDescriptor descriptor, CustomClassLoader classLoader) throws PluginLoadException {
        try {
            Class<?> pluginClass = classLoader.loadClass(descriptor.getPluginClass());
            return (Plugin) pluginClass.getDeclaredConstructor().newInstance();
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause() != null ? e.getCause() : e;
            throw new PluginLoadException("Failed to create plugin", cause);
        } catch (Throwable e) {
            throw new PluginLoadException("Failed to create plugin", e);
        }
    }
}