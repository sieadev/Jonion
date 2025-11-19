package dev.siea.jonion.exceptions;

public class PluginException extends Exception {
    public PluginException(String message, Exception e) {
        super(message, e);
    }

    public PluginException(String message, Throwable cause) {
        super(message, cause);
    }
}