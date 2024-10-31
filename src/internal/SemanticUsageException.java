package internal;

public class SemanticUsageException extends SemanticException {

    public SemanticUsageException(String msg) {
        super("Usage Exception: " + msg);
    }

}
