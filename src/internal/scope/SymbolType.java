package internal.scope;

public enum SymbolType {
    Variable,
    Function;

    public String toString() {
        switch (this) {
            case SymbolType.Variable:
                return "Variable";
            case SymbolType.Function:
                return "Function";
            default:
                return "Unknown";
        }
    }
}
