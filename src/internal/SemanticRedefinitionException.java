package internal;

public class SemanticRedefinitionException extends SemanticException {

    public SemanticRedefinitionException(String name) {
        super("The symbol " + name + " has already been defined.");
    }

}
