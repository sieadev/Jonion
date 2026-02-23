package dev.siea.jonion.manager;

import dev.siea.jonion.PluginWrapper;
import dev.siea.jonion.configuration.finder.PluginConfigurationFinder;
import dev.siea.jonion.configuration.finder.YamlConfigurationFinder;
import dev.siea.jonion.depedency.PluginDependency;
import dev.siea.jonion.descriptor.PluginDescriptor;
import dev.siea.jonion.descriptor.finder.PluginDescriptorFinder;
import dev.siea.jonion.descriptor.finder.YamlDescriptorFinder;
import dev.siea.jonion.exceptions.CircularDependencyException;
import dev.siea.jonion.exceptions.MissingDependencyException;
import dev.siea.jonion.exceptions.PluginLoadException;
import dev.siea.jonion.lifecycle.PluginState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Base implementation of {@link PluginManager} that discovers plugins from a
 * directory, creates wrappers, and loads them in dependency order.
 * <p>
 * Subclasses typically add lifecycle behavior (e.g. start/stop) by overriding
 * or extending the load/unload flow. This class handles:
 * </p>
 * <ul>
 *   <li>Scanning a plugin directory for JAR files</li>
 *   <li>Finding descriptors and configurations via pluggable finders</li>
 *   <li>Creating {@link PluginWrapper} instances and sorting by dependencies</li>
 *   <li>Loading plugins and detecting circular or missing dependencies</li>
 * </ul>
 *
 * @see PluginManager
 * @see PluginWrapper
 * @see PluginDescriptor
 * @see PluginConfigurationFinder
 */
public abstract class AbstractPluginManager implements PluginManager {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final List<PluginWrapper> pluginWrappers = new ArrayList<>();
    private final PluginDescriptorFinder descriptorFinder;
    private final PluginConfigurationFinder configurationFinder;
    private final Path pluginDirectory;

    /** Creates a manager that scans the default {@code plugins} directory with YAML descriptor and configuration finders. */
    public AbstractPluginManager() {
        this(Paths.get("plugins"));
    }

    /**
     * Creates a manager that scans the given directory with the default YAML descriptor finder.
     *
     * @param directory the path to the plugin directory (created if it does not exist)
     */
    public AbstractPluginManager(Path directory) {
        this(directory, new YamlDescriptorFinder());
    }

    /**
     * Creates a manager with a custom descriptor finder and default YAML configuration finder.
     *
     * @param directory        the path to the plugin directory (created if it does not exist)
     * @param descriptorFinder the finder used to read plugin descriptors from JARs
     */
    public AbstractPluginManager(Path directory, PluginDescriptorFinder descriptorFinder) {
        this(directory, descriptorFinder, new YamlConfigurationFinder());
    }

    /**
     * Creates a manager with custom descriptor and configuration finders.
     *
     * @param directory             the path to the plugin directory (created if it does not exist)
     * @param descriptorFinder     the finder used to read plugin descriptors from JARs
     * @param configurationFinder  the finder used to locate plugin configuration files
     */
    public AbstractPluginManager(Path directory, PluginDescriptorFinder descriptorFinder, PluginConfigurationFinder configurationFinder) {
        pluginDirectory = directory;

        if (!Files.exists(pluginDirectory)) {
            try {
                Files.createDirectories(pluginDirectory);
            } catch (IOException e) {
                logger.error("Failed to create plugin directory", e);
            }
        }

        this.descriptorFinder = descriptorFinder;
        this.configurationFinder = configurationFinder;
        createPluginWrappers();
        loadPlugins();
    }

    @Override
    public List<PluginWrapper> getPlugins() {
        return pluginWrappers;
    }

    @Override
    public List<PluginWrapper> getPlugins(PluginState state) {
        return pluginWrappers.stream()
                .filter(pluginWrapper -> pluginWrapper.getState() == state)
                .collect(Collectors.toList());
    }

    @Override
    public PluginWrapper getPlugin(String pluginId) {
        return pluginWrappers.stream()
                .filter(pluginWrapper -> pluginWrapper.getPluginDescriptor().getPluginId().equals(pluginId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Scans the plugin directory for JAR files and creates a {@link PluginWrapper} for each.
     * Duplicate plugin IDs and paths without a valid descriptor are skipped (and logged).
     */
    protected void createPluginWrappers() {
        try (Stream<Path> paths = Files.walk(pluginDirectory)) {
            paths.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".jar"))
                    .forEach(this::createPluginWrapperFromPath);
        } catch (Exception e) {
            logger.error("Error while creating plugin wrappers", e);
        }
    }

    private void loadPlugins() {
        List<PluginWrapper> sortedPlugins = sortPluginsByDependencies(pluginWrappers);
        sortedPlugins.forEach(pluginWrapper -> {
            if (pluginWrapper.getState() != PluginState.CREATED) {
                return;
            }
            try {
                pluginWrapper.load();
                logger.debug("Loaded plugin: {}", pluginWrapper.getPluginDescriptor().getPluginId());
            } catch (PluginLoadException e) {
                logger.error("Failed to load plugin: {}", pluginWrapper.getPluginDescriptor().getPluginId(), e);
            }
        });
    }

    /** Unloads all currently loaded plugins and clears the internal list of wrappers. */
    protected void unloadPlugins() {
        pluginWrappers.forEach(pluginWrapper -> {
            if (pluginWrapper.getState() == PluginState.LOADED) {
                pluginWrapper.unload();
            }
        });

        pluginWrappers.clear();
    }

    /**
     * Unloads a single plugin by ID and removes it from the manager.
     *
     * @param pluginId the ID of the plugin to unload
     */
    protected void unloadPlugin(String pluginId) {
        PluginWrapper pluginWrapper = getPlugin(pluginId);
        if (pluginWrapper != null) {
            pluginWrapper.unload();
            pluginWrappers.remove(pluginWrapper);
        }
    }

    /** Unloads all plugins, rescans the plugin directory, and loads plugins again in dependency order. */
    protected void reloadPlugins() {
        unloadPlugins();
        createPluginWrappers();
        loadPlugins();
    }

    private List<PluginWrapper> sortPluginsByDependencies(List<PluginWrapper> plugins) {
        Map<String, PluginWrapper> pluginMap = plugins.stream()
                .collect(Collectors.toMap(p -> p.getPluginDescriptor().getPluginId(), p -> p));
        List<PluginWrapper> sortedPlugins = new ArrayList<>();
        Set<String> visited = new HashSet<>();

        for (PluginWrapper plugin : plugins) {
            try {
                visit(plugin, pluginMap, sortedPlugins, visited, new HashSet<>());
            } catch (CircularDependencyException | MissingDependencyException e) {
                logger.error(e.getMessage());
                plugin.setState(PluginState.FAILED);
            }
        }

        return sortedPlugins;
    }

    private void visit(PluginWrapper plugin, Map<String, PluginWrapper> pluginMap, List<PluginWrapper> sortedPlugins, Set<String> visited, Set<String> stack) throws CircularDependencyException, MissingDependencyException {
        String pluginId = plugin.getPluginDescriptor().getPluginId();
        if (visited.contains(pluginId)) {
            return;
        }
        if (stack.contains(pluginId)) {
            throw new CircularDependencyException("Circular dependency detected: " + pluginId);
        }
        stack.add(pluginId);
        for (PluginDependency dependency : plugin.getPluginDescriptor().getDependencies()) {
            PluginWrapper dependencyPlugin = pluginMap.get(dependency.getPluginId());
            if (dependencyPlugin != null) {
                visit(dependencyPlugin, pluginMap, sortedPlugins, visited, stack);
            } else if (!dependency.isOptional()) {
                throw new MissingDependencyException("Missing required dependency: " + dependency.getPluginId());
            }
        }
        stack.remove(pluginId);
        visited.add(pluginId);
        sortedPlugins.add(plugin);
    }

    /**
     * Creates a single {@link PluginWrapper} from a JAR path and adds it to the internal list
     * if the descriptor is valid and the plugin ID is not already registered.
     *
     * @param path the path to the plugin JAR file
     */
    protected void createPluginWrapperFromPath(Path path) {
        PluginDescriptor pluginDescriptor = descriptorFinder.findPluginDescriptor(path);
        logger.debug("Creating plugin wrapper from path: {}", path);
        if (pluginDescriptor == null) {
            logger.error("DescriptionFinder was unable to find a plugin descriptor for path: {}", path);
            return;
        }
        String pluginId = pluginDescriptor.getPluginId();
        if (getPlugin(pluginId) != null) {
            logger.error("Duplicate found. A plugin with the ID {} is already registered.", pluginId);
            return;
        }
        logger.debug("Found plugin descriptor for {}", pluginId);
        pluginWrappers.add(new PluginWrapper(this, pluginDescriptor, configurationFinder, path));
    }
}
