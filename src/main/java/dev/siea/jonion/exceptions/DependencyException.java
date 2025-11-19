package dev.siea.jonion.exceptions;

/**
 * Exception thrown when a dependency issue is detected in the dependency graph.
 * <p>
 * This exception is used to signal that a dependency issue was detected in the
 * dependency graph of the system
 * </p>
 */
public class DependencyException extends Exception {

    /**
     * Constructs a new DependencyException with the specified detail message.
     *
     * @param message the detail message
     */
    public DependencyException(String message) {
        super(message);
    }
}