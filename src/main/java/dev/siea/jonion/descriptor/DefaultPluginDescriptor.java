package dev.siea.jonion.descriptor;

import dev.siea.jonion.depedency.PluginDependency;

import java.util.ArrayList;
import java.util.List;

public class DefaultPluginDescriptor implements PluginDescriptor {
    private final String pluginId;
    private final String description;
    private final String version;
    private final String pluginClass;
    private final List<String> authors;
    private final String license;
    private final List<PluginDependency> dependencies;

    public DefaultPluginDescriptor(String pluginId, String description, String version, String pluginClass, List<String> authors, String license) {
        dependencies = new ArrayList<>();
        this.pluginId = pluginId;
        this.description = description;
        this.version = version;
        this.pluginClass = pluginClass;
        this.authors = authors;
        this.license = license;
    }

    public void addDependency(PluginDependency dependency) {
        this.dependencies.add(dependency);
    }

    @Override
    public String getPluginId() {
        return pluginId;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public String getPluginClass() {
        return pluginClass;
    }

    @Override
    public List<String> getAuthors() {
        return authors;
    }

    @Override
    public String getLicense() {
        return license;
    }

    @Override
    public List<PluginDependency> getDependencies() {
        return dependencies;
    }
}
