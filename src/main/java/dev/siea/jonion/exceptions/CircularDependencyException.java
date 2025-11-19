package dev.siea.jonion.exceptions;

/**
 * Exception thrown when a circular dependency is detected in the dependency graph.
 * <p>
 * This exception is used to signal that a circular dependency was detected in the
 * dependency graph of the system, which would cause a deadlock if unresolved.
 * </p>
 */
public class CircularDependencyException extends DependencyException {

    /**
     * Constructs a new CircularDependencyException with the specified detail message.
     *
     * @param message the detail message
     */
    public CircularDependencyException(String message) {
        super(message);
    }
}

