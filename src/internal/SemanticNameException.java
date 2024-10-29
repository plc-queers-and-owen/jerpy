package internal;

public class SemanticNameException extends SemanticException {

    public SemanticNameException(String name) {
        super("Invalid Name Error: The symbol name \"" + name + "\" is invalid.");
    }

}
