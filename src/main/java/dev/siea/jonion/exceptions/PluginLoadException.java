package dev.siea.jonion.exceptions;

public class PluginLoadException extends PluginException {
    public PluginLoadException(String message, Exception e) {
        super(message, e);
    }

    public PluginLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
