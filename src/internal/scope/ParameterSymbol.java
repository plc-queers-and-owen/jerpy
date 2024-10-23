package internal.scope;

import internal.eval.Type;
import internal.eval.TypedValue;
import internal.nodes.FuncDefParamNode;

public class ParameterSymbol extends SymbolItem<FuncDefParamNode> {
    private TypedValue value;

    public ParameterSymbol(String symbol, int definedAt, FuncDefParamNode target) {
        super(symbol, definedAt, target);
        this.value = null;
    }

    public ParameterSymbol(String symbol, int definedAt, FuncDefParamNode target, TypedValue value) {
        super(symbol, definedAt, target);
        this.value = value;
    }

    public static ParameterSymbol from(FuncDefParamNode target) {
        return new ParameterSymbol(target.getSymbol(), target.getLineNumber(), target);
    }

    public Type getType() {
        return this.target.getType().getType();
    }

    public String getName() {
        return this.target.getSymbol();
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
        return SymbolType.Parameter;
    }
}
