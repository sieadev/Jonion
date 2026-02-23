package dev.siea.jonion.dependency;

public class PluginDependency {
    private final String pluginId;
    private final boolean optional;

    public PluginDependency(String pluginId, boolean optional) {
        this.pluginId = pluginId;
        this.optional = optional;
    }

    public String getPluginId() {
        return this.pluginId;
    }

    public boolean isOptional() {
        return this.optional;
    }

    public boolean equals(Object dependency) {
        if (this == dependency) {
            return true;
        } else if (!(dependency instanceof PluginDependency that)) {
            return false;
        } else {
            return this.optional == that.optional && this.pluginId.equals(that.pluginId);
        }
    }
}
