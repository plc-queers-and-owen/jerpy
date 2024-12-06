package internal.scope.builtins;

import java.util.List;

import internal.ExecutionException;
import internal.SemanticException;
import internal.SemanticTypeException;
import internal.SemanticUsageException;
import internal.eval.Type;
import internal.eval.TypedValue;
import internal.nodes.ExprNode;
import internal.nodes.ParamsNode;
import internal.scope.FunctionSymbol;
import internal.scope.Scope;

public class LengthFunction implements FunctionSymbol {

    @Override
    public String name() {
        return "length";
    }

    @Override
    public int paramCount() {
        return 1;
    }

    @Override
    public Type paramType(int idx) {
        if (idx == 0) {
            return Type.String;
        } else {
            return null;
        }
    }

    @Override
    public boolean isBuiltin() {
        return true;
    }

    @Override
    public boolean validateParameters(Scope scope, ParamsNode params) throws SemanticException {
        List<ExprNode> p = params.params();

        if (p.size() != 1) {
            new SemanticUsageException("::" + this.name() + "(...) requires exactly " + this.paramCount()
                    + " parameters, but got " + p.size()).report(params);
            return false;
        }

        if (p.getFirst().inferType(scope) != Type.String) {
            new SemanticTypeException(p.getFirst().inferType(scope),
                    Type.String).report(p.getFirst());
            return false;
        }

        return true;
    }

    @Override
    public TypedValue evaluate(Scope scope, ParamsNode params) throws ExecutionException, SemanticException {
        String param = params.params().getFirst().evaluate(scope).getString();
        return new TypedValue(param.length());
    }

    @Override
    public Type returnType() {
        return Type.Integer;
    }

}
