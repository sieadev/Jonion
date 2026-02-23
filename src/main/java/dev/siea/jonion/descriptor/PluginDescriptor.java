package dev.siea.jonion.descriptor;

import dev.siea.jonion.dependency.PluginDependency;

import java.util.List;

public interface PluginDescriptor {
    String getPluginId();
    String getDescription();
    String getVersion();
    String getPluginClass();
    List<String> getAuthors();
    String getLicense();
    List<PluginDependency> getDependencies();
}
