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

public abstract class AbstractPluginManager implements PluginManager {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final List<PluginWrapper> pluginWrappers = new ArrayList<>();
    private final PluginDescriptorFinder descriptorFinder;
    private final PluginConfigurationFinder configurationFinder;
    private final Path pluginDirectory;

    public AbstractPluginManager() {
        this(Paths.get("plugins"));
    }

    public AbstractPluginManager(Path directory) {
        this(directory, new YamlDescriptorFinder());
    }

    public AbstractPluginManager(Path directory, PluginDescriptorFinder descriptorFinder) {
        this(directory, descriptorFinder, new YamlConfigurationFinder());
    }

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

    protected void unloadPlugins() {
        pluginWrappers.forEach(pluginWrapper -> {
            if (pluginWrapper.getState() == PluginState.LOADED) {
                pluginWrapper.unload();
            }
        });

        pluginWrappers.clear();
    }

    protected void unloadPlugin(String pluginId) {
        PluginWrapper pluginWrapper = getPlugin(pluginId);
        if (pluginWrapper != null) {
            pluginWrapper.unload();
            pluginWrappers.remove(pluginWrapper);
        }
    }

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
