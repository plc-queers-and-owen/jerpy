package internal.scope.builtins;

import java.util.List;

import internal.SemanticException;
import internal.SemanticTypeException;
import internal.SemanticUsageException;
import internal.eval.Type;
import internal.eval.TypedValue;
import internal.nodes.ExprNode;
import internal.nodes.ParamsNode;
import internal.scope.FunctionSymbol;
import internal.scope.Scope;

public class ConcatFunction implements FunctionSymbol {

    @Override
    public String name() {
        return "concat";
    }

    @Override
    public int paramCount() {
        return 2;
    }

    @Override
    public Type paramType(int idx) {
        if (idx < 2) {
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

        if (p.size() != 2) {
            new SemanticUsageException("::" + this.name() + "(...) requires exactly " + this.paramCount()
                    + " parameters, but got " + p.size()).report(params);
            return false;
        }

        if (p.getFirst().inferType(scope) != Type.String) {
            new SemanticTypeException(p.getFirst().inferType(scope),
                    Type.String).report(p.getFirst());
            return false;
        }

        if (p.get(1).inferType(scope) != Type.String) {
            new SemanticTypeException(p.get(1).inferType(scope),
                    Type.String).report(p.get(1));
            return false;
        }

        return true;
    }

    @Override
    public TypedValue evaluate(Scope scope, ParamsNode params) {
        return null;
    }

    @Override
    public Type returnType() {
        return Type.String;
    }

}
