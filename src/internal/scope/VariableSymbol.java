package internal.scope;

import internal.ParseHaltException;
import internal.eval.Type;
import internal.eval.TypedValue;
import internal.nodes.VariableDeclarationNode;

public class VariableSymbol extends SymbolItem<VariableDeclarationNode> {
    private TypedValue value;
    public VariableSymbol(String symbol, int definedAt, VariableDeclarationNode target) {
        super(symbol, definedAt, target);
        this.value = null;
    }

    public VariableSymbol(String symbol, int definedAt, VariableDeclarationNode target, TypedValue value) {
        super(symbol, definedAt, target);
        this.value = value;
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

    public TypedValue getValue() {
        return this.value;
    }

    public void setValue(TypedValue value) {
        this.value = value;
    }

    public boolean hasValue() {
        return this.value != null;
    }

    @Override
    public SymbolType getSymbolType() {
        return SymbolType.Variable;
    }
}
