package internal.nodes;

import java.util.List;

import internal.ExecutionException;
import internal.ParseHaltException;
import internal.ParseUnexpectedTokenException;
import internal.PeekingArrayIterator;
import internal.SemanticException;
import internal.eval.Type;
import internal.eval.TypedValue;
import internal.scope.FunctionSymbol;
import internal.scope.Scope;
import provided.TokenType;

/**
 * A function call operand
 */
public class FuncCallNode extends OperandNode {
    private final String name;
    private final ParamsNode params;

    protected FuncCallNode(String filename, int lineNumber, String name, ParamsNode params) {
        super(filename, lineNumber);
        this.name = name;
        this.params = params;
        this.adopt();
    }

    // Needs completion
    public static FuncCallNode parse(PeekingArrayIterator it) throws ParseUnexpectedTokenException, ParseHaltException {
        int line = it.expect(TokenType.FC_HEADER).getLineNum();
        String name = it.expect(TokenType.ID_KEYWORD).getToken();
        it.expect(TokenType.L_BRACKET);
        ParamsNode params = ParamsNode.parse(it);
        it.expect("]");
        return new FuncCallNode(it.getCurrentFilename(), line, name, params);
    }

    @Override
    public String convertToJott() {
        return "::" + name + "[" + params.convertToJott() + "]";
    }

    @Override
    public boolean validateTree(Scope scope) {
        // System.out.println(this.convertToJott() + " :: " +
        // Integer.toString(this.getLineNumber()));
        try {
            FunctionSymbol calledFunc = scope.getScope(name).getContext();
            return this.params.validateTree(scope)
                    && calledFunc.validateParameters(scope, this.params);
        } catch (SemanticException e) {
            e.report(this);
            return false;
        }
    }

    @Override
    public TypedValue evaluate(Scope scope) throws ExecutionException, SemanticException {
        return this.getDefinition(scope).evaluate(scope, params);
    }

    @Override
    public List<Node> getChildren() {
        return List.of(this.params);
    }

    public FunctionSymbol getDefinition(Scope scope) throws SemanticException {
        return scope.getScope(this.name).getContext();
    }

    @Override
    public Type inferType(Scope scope) throws SemanticException {
        return this.getDefinition(scope).returnType();
    }
}
