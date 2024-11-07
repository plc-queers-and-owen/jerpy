package internal.scope;

import internal.SemanticException;
import internal.SemanticTypeException;
import internal.SemanticUsageException;
import internal.eval.Type;
import internal.nodes.FuncDefParamNode;
import internal.nodes.FunctionDefNode;
import internal.nodes.ParamsNode;

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
        throw new UnsupportedOperationException("Unimplemented method 'execute'");
    }

    @Override
    public boolean validateParameters(Scope scope, ParamsNode params) throws SemanticException {
        if (params.params().size() != this.paramCount()) {
            new SemanticUsageException("::" + this.name() + "(...) requires exactly " + this.paramCount()
                    + " parameters, but got " + params.params().size()).report(params);
            return false;
        }
        for (int p = 0; p < this.paramCount(); p++) {
            try {
                if (params.params().get(p).inferType(scope) != this.paramType(p)) {
                    new SemanticTypeException(params.params().get(p).inferType(scope),
                            this.paramType(p)).report(params.params().get(p));
                    return false;
                }
            } catch (SemanticException e) {
                e.report(params.params().get(p));
                return false;
            }
        }
        return true;
    }

    @Override
    public Type returnType() {
        return this.getTarget().getReturnType();
    }
}
