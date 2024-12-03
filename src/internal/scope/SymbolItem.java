package internal.scope;

import internal.nodes.Node;

public abstract class SymbolItem<T extends Node> {
    protected final int definedAt;

    public int getDefinedAt() {
        return definedAt;
    }

    protected final T target;

    public T getTarget() {
        return target;
    }

    protected final String symbol;

    public String getSymbol() {
        return symbol;
    }

    public SymbolItem(String symbol, int definedAt, T target) {
        this.symbol = symbol;
        this.definedAt = definedAt;
        this.target = target;
    }

    public abstract SymbolType getSymbolType();

    public void initialize() {

    }

    public boolean isInitialized() {
        return true;
    }
}
