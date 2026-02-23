package dev.siea.jonion.impl;

import dev.siea.jonion.Plugin;

/**
 * Base plugin implementation with start/stop lifecycle hooks.
 * <p>
 * Extends {@link Plugin} and adds {@link #start()} and {@link #stop()} methods that
 * {@link dev.siea.jonion.manager.DefaultPluginManager} calls after loading and before
 * unloading. Subclass this and override {@link #start()} and {@link #stop()} to run
 * logic when the plugin is enabled or disabled.
 * </p>
 *
 * @see Plugin
 * @see dev.siea.jonion.manager.DefaultPluginManager
 */
public class SimplePlugin extends Plugin {

    /**
     * Called when the plugin is started. Override to perform setup (e.g. register
     * listeners or schedule tasks).
     */
    public void start() {
       //To be overridden by subclasses
    }

    /**
     * Called when the plugin is stopped. Override to release resources and clean up.
     */
    public void stop() {
       //To be overridden by subclasses
    }
}
