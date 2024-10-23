package internal.scope;

import internal.ParseHaltException;
import internal.eval.Type;
import internal.nodes.VariableDeclarationNode;

public class VariableSymbol extends SymbolItem<VariableDeclarationNode> {
    public VariableSymbol(String symbol, int definedAt, VariableDeclarationNode target) {
        super(symbol, definedAt, target);
    }

    public static VariableSymbol from(VariableDeclarationNode target) throws ParseHaltException {
        return new VariableSymbol(target.getSymbol(), target.getLineNumber(), target);
    }

    public Type getType() {
        return this.target.getType().getType();
    }

    public String getName() {
        return this.target.getName();
    }
}
