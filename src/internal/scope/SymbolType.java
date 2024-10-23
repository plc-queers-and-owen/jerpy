package internal.scope;

public enum SymbolType {
    Variable,
    Parameter,
            Function;

    public String toString() {
        switch (this) {
            case SymbolType.Variable:
                return "Variable";
            case SymbolType.Function:
                return "Function";
            case SymbolType.Parameter:
                return "Parameter";
            default:
                return "Unknown";
        }
    }
}
