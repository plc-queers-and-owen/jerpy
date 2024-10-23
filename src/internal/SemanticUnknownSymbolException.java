package internal;

public class SemanticUnknownSymbolException extends SemanticException {

    public SemanticUnknownSymbolException(String symbol) {
        super("Unknown symbol: " + symbol);
    }

}
