package internal.scope;

import internal.nodes.FunctionDefNode;

public class FunctionSymbol extends SymbolItem<FunctionDefNode> {
    public FunctionSymbol(String symbol, int definedAt, FunctionDefNode target) {
        super(symbol, definedAt, target);
    }
}
