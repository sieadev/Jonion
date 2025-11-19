package dev.siea.jonion.manager;

import dev.siea.jonion.PluginWrapper;
import dev.siea.jonion.lifecycle.PluginState;

import java.util.List;

public interface PluginManager {
    List<PluginWrapper> getPlugins();
    List<PluginWrapper> getPlugins(PluginState state);
    PluginWrapper getPlugin(String pluginId);
}
