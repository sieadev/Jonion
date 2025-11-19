package dev.siea.jonion.exceptions;

public class ConfigSaveException extends ConfigException {
    public ConfigSaveException(String message) {
        super(message);
    }

    public ConfigSaveException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigSaveException(Throwable cause) {
        super(cause);
    }

    public ConfigSaveException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
