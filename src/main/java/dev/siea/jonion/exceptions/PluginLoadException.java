package dev.siea.jonion.exceptions;

/**
 * Thrown when a plugin cannot be loaded or instantiated (e.g. class not found,
 * instantiation error, or dependency failure).
 * <p>
 * Used by {@link dev.siea.jonion.PluginWrapper#load()} and
 * {@link dev.siea.jonion.PluginFactory#createPlugin} when the plugin main class
 * cannot be loaded or its constructor fails.
 * </p>
 *
 * @see dev.siea.jonion.PluginWrapper
 * @see dev.siea.jonion.PluginFactory
 */
public class PluginLoadException extends PluginException {
    /**
     * Constructs a new PluginLoadException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param e      the cause
     */
    public PluginLoadException(String message, Exception e) {
        super(message, e);
    }

    /**
     * Constructs a new PluginLoadException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause
     */
    public PluginLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
