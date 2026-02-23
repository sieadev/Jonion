package dev.siea.jonion.exceptions;

/**
 * Base exception for plugin-related errors (load, lifecycle, etc.).
 * <p>
 * Subclassed by more specific exceptions such as {@link PluginLoadException}.
 * Allows callers to catch all plugin failures without depending on concrete types.
 * </p>
 *
 * @see PluginLoadException
 */
public class PluginException extends Exception {
    /**
     * Constructs a new PluginException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param e      the cause
     */
    public PluginException(String message, Exception e) {
        super(message, e);
    }

    /**
     * Constructs a new PluginException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause
     */
    public PluginException(String message, Throwable cause) {
        super(message, cause);
    }
}