package dev.siea.jonion.exceptions;

/**
 * Exception thrown when a dependency is missing in the dependency graph.
 * <p>
 * This exception is used to signal that a dependency is missing in the
 * dependency graph of the system.
 * </p>
 */
public class MissingDependencyException extends DependencyException {

    /**
     * Constructs a new MissingDependencyException with the specified detail message.
     *
     * @param message the detail message
     */
    public MissingDependencyException(String message) {
        super(message);
    }
}