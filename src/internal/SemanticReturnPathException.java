package internal;

public class SemanticReturnPathException extends SemanticException {

    public SemanticReturnPathException(String reason) {
        super("Invalid return path for function: " + reason);
    }

}
