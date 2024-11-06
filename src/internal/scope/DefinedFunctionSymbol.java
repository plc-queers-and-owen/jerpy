package internal.scope;

import internal.eval.Type;
import internal.nodes.FuncDefParamNode;
import internal.nodes.FunctionDefNode;

public class DefinedFunctionSymbol extends SymbolItem<FunctionDefNode> implements FunctionSymbol {
    public DefinedFunctionSymbol(String symbol, int definedAt, FunctionDefNode target) {
        super(symbol, definedAt, target);
    }

    @Override
    public SymbolType getSymbolType() {
        return SymbolType.Function;
    }

    @Override
    public String name() {
        return this.getTarget().name;
    }

    @Override
    public int paramCount() {
        return this.getTarget().params.size();
    }

    @Override
    public Type paramType(int idx) {
        FuncDefParamNode param = this.getTarget().params.get(idx);
        if (param != null) {
            return param.getType().getType();
        } else {
            return null;
        }
    }

    @Override
    public boolean isBuiltin() {
        return false;
    }

    @Override
    public void execute() {
        // TODO(phase-4)
        // Don't know if we'll end up needing this here,
        // but its useful for builtins if its in FuncitonSymbol
        throw new UnsupportedOperationException("Execute not defined yet");
    }
}
