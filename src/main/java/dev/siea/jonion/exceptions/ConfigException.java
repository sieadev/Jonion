package dev.siea.jonion.exceptions;

/**
 * Base exception for configuration-related errors (read, parse, validation).
 * <p>
 * Subclassed by more specific exceptions such as {@link ConfigSaveException}.
 * Unchecked so configuration failures can be thrown from code that does not
 * declare {@code throws}.
 * </p>
 *
 * @see ConfigSaveException
 */
public class ConfigException extends RuntimeException {
    /**
     * Constructs a new ConfigException with the specified detail message.
     *
     * @param message the detail message
     */
    public ConfigException(String message) {
        super(message);
    }

    /**
     * Constructs a new ConfigException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause
     */
    public ConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new ConfigException with the specified cause.
     *
     * @param cause the cause
     */
    public ConfigException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new ConfigException with the specified detail message, cause,
     * suppression enabled or disabled, and writable stack trace enabled or disabled.
     *
     * @param message             the detail message
     * @param cause               the cause
     * @param enableSuppression   whether suppression is enabled
     * @param writableStackTrace whether the stack trace is writable
     */
    public ConfigException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
