package dev.siea.jonion;

import com.pixelservices.logger.Logger;
import com.pixelservices.logger.LoggerFactory;
import dev.siea.jonion.configuration.PluginConfig;
import dev.siea.jonion.configuration.finder.PluginConfigurationFinder;
import dev.siea.jonion.descriptor.PluginDescriptor;

public abstract class Plugin {
    private PluginWrapper pluginWrapper;
    private PluginDescriptor pluginDescriptor;
    private PluginConfigurationFinder configurationFinder;
    private Logger logger;

    final void load(PluginWrapper pluginWrapper, PluginDescriptor pluginDescriptor, PluginConfigurationFinder configurationFinder) {
        this.pluginWrapper = pluginWrapper;
        this.pluginDescriptor = pluginDescriptor;
        this.configurationFinder = configurationFinder;
        logger = LoggerFactory.getLogger(pluginDescriptor.getPluginId());
    }

    public PluginWrapper getPluginWrapper() {
        return pluginWrapper;
    }

    public final PluginDescriptor getMetaData(){
        return pluginDescriptor;
    }

    public final Logger getLogger() {
        return logger;
    }

    public final PluginConfig getDefaultConfig() {
        return configurationFinder.findPluginConfiguration(pluginDescriptor.getPluginId(), pluginWrapper.getPath());
    }

    public final PluginConfig getConfig(String configFileName) {
        return configurationFinder.findPluginConfiguration(pluginDescriptor.getPluginId(), pluginWrapper.getPath(), configFileName);
    }
}
