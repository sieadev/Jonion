package dev.siea.jonion.exceptions;

/**
 * Thrown when a plugin configuration file cannot be saved (e.g. I/O error or
 * permission denied).
 * <p>
 * Extends {@link ConfigException} for configuration-related failures that occur
 * during write/save operations.
 * </p>
 *
 * @see ConfigException
 */
public class ConfigSaveException extends ConfigException {
    /**
     * Constructs a new ConfigSaveException with the specified detail message.
     *
     * @param message the detail message
     */
    public ConfigSaveException(String message) {
        super(message);
    }

    /**
     * Constructs a new ConfigSaveException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause
     */
    public ConfigSaveException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new ConfigSaveException with the specified cause.
     *
     * @param cause the cause
     */
    public ConfigSaveException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new ConfigSaveException with the specified detail message, cause,
     * suppression enabled or disabled, and writable stack trace enabled or disabled.
     *
     * @param message             the detail message
     * @param cause               the cause
     * @param enableSuppression   whether suppression is enabled
     * @param writableStackTrace whether the stack trace is writable
     */
    public ConfigSaveException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
