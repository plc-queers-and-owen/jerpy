package internal;

public class SemanticUninitializedVariableException extends SemanticException {

    public SemanticUninitializedVariableException(String symbol) {
        super("The symbol " + symbol + " has been declared, but no value has been set.");
    }

}
