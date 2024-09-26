package internal;

public class UnreachableException extends RuntimeException {
    public UnreachableException() {
        super("unreachable");
    }
}
