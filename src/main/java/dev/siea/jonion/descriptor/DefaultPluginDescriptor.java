package dev.siea.jonion.descriptor;

import dev.siea.jonion.depedency.PluginDependency;

import java.util.ArrayList;
import java.util.List;

/**
 * Mutable implementation of {@link PluginDescriptor} used by descriptor finders to build
 * metadata from YAML or XML. Dependencies are added via {@link #addDependency(PluginDependency)}
 * after construction.
 *
 * @see PluginDescriptor
 * @see dev.siea.jonion.descriptor.finder.YamlDescriptorFinder
 * @see dev.siea.jonion.descriptor.finder.XmlDescriptorFinder
 */
public class DefaultPluginDescriptor implements PluginDescriptor {
    private final String pluginId;
    private final String description;
    private final String version;
    private final String pluginClass;
    private final List<String> authors;
    private final String license;
    private final List<PluginDependency> dependencies;

    /**
     * Creates a new descriptor with the given metadata. Dependencies list is initially empty;
     * use {@link #addDependency(PluginDependency)} to add dependencies.
     *
     * @param pluginId     unique plugin identifier
     * @param description  short description (may be empty)
     * @param version      version string
     * @param pluginClass  fully qualified plugin main class name
     * @param authors      list of author names (may be empty or null)
     * @param license      license identifier or name
     */
    public DefaultPluginDescriptor(String pluginId, String description, String version, String pluginClass, List<String> authors, String license) {
        dependencies = new ArrayList<>();
        this.pluginId = pluginId;
        this.description = description;
        this.version = version;
        this.pluginClass = pluginClass;
        this.authors = authors;
        this.license = license;
    }

    /**
     * Appends a dependency to this descriptor.
     *
     * @param dependency the dependency to add
     */
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
