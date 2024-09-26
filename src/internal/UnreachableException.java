package internal;

/**
 * Internal error, thrown when we reach an area which should be unreachable
 */
public class UnreachableException extends RuntimeException {
    /**
     * Constructs an UnreachableException
     */
    public UnreachableException() {
        super("unreachable");
    }
}
