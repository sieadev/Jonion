package dev.siea.jonion.descriptor.finder;

import dev.siea.jonion.descriptor.PluginDescriptor;

import java.nio.file.Path;

public interface PluginDescriptorFinder {
    PluginDescriptor findPluginDescriptor(Path path);
}
