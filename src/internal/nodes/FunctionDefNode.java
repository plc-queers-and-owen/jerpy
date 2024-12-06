package internal.nodes;

import java.util.ArrayList;
import java.util.List;

import internal.ExecutionException;
import internal.ParseHaltException;
import internal.PeekingArrayIterator;
import internal.SemanticException;
import internal.SemanticNameException;
import internal.scope.DefinedFunctionSymbol;
import internal.scope.LocalScope;
import internal.scope.Scope;
import internal.eval.Type;
import internal.eval.TypedValue;
import provided.TokenType;

/**
 * A function definition, the top level element of a Jott program
 */
public class FunctionDefNode extends Node {

    public String name;
    public List<FuncDefParamNode> params;
    public FunctionReturnNode returnType;
    public FunctionBodyNode body;

    protected FunctionDefNode(String filename, int lineNumber, String name, List<FuncDefParamNode> params,
            FunctionReturnNode returnType, FunctionBodyNode body) {
        super(filename, lineNumber);
        this.name = name;
        this.params = params;
        this.returnType = returnType;
        this.body = body;
        this.adopt();
    }

    public static FunctionDefNode parse(PeekingArrayIterator it) throws ParseHaltException {
        int line = it.expect("Def").getLineNum();
        String name = it.expect(TokenType.ID_KEYWORD).getToken();
        it.expect(TokenType.L_BRACKET);

        ArrayList<FuncDefParamNode> params = new ArrayList<>();
        // Check to see if we have any params to try to parse
        if (it.peekExpectSafe(TokenType.ID_KEYWORD) != -1) {
            params.add(FuncDefParamNode.parse(it));
            // Try to parse params as long as we keep seeing a comma
            while (it.peekExpectSafe(TokenType.COMMA) != -1) {
                it.expect(TokenType.COMMA);
                params.add(FuncDefParamNode.parse(it));
            }
        }

        it.expect(TokenType.R_BRACKET);
        it.expect(TokenType.COLON);
        FunctionReturnNode returnType = FunctionReturnNode.parse(it);

        it.expect(TokenType.L_BRACE);
        FunctionBodyNode body = FunctionBodyNode.parse(it);
        it.expect(TokenType.R_BRACE);

        return new FunctionDefNode(it.getCurrentFilename(), line, name, params, returnType, body);
    }

    @Override
    public String convertToJott() {
        StringBuilder output = new StringBuilder();
        output.append("Def ");
        output.append(this.name);
        output.append('[');

        if (!this.params.isEmpty()) {
            output.append(this.params.get(0).convertToJott());

            for (int i = 1; i < this.params.size(); i++) {
                output.append(',');
                output.append(this.params.get(i).convertToJott());
            }
        }

        output.append(']');
        output.append(':');
        output.append(this.returnType.convertToJott());
        output.append('{');
        output.append(this.body.convertToJott());
        output.append('}');

        return output.toString();
    }

    @Override
    public boolean validateTree(Scope scope) {
        // System.out.println(this.convertToJott() + " :: " +
        // Integer.toString(this.getLineNumber()));
        if (!validateId(name)) {
            new SemanticNameException(this.name).report(this);
            return false;
        }

        if (!this.returnType.validateTree(scope)) {
            return false;
        }

        try {
            scope.define(new DefinedFunctionSymbol(this.getSymbol(), getLineNumber(), this));
            scope.enableScope(this.getSymbol());
        } catch (SemanticException e) {
            e.report(this);
            return false;
        }

        if (!this.params.stream().allMatch(p -> p.validateTree(scope))) {
            scope.clearScope();
            return false;
        }

        if (!this.body.validateTree(scope)) {
            scope.clearScope();
            return false;
        } else {
            scope.clearScope();
            scope.finish(this.getSymbol());
            return true;
        }
    }

    public TypedValue call(Scope scope, ParamsNode callParams) throws SemanticException, ExecutionException {
        String parentScope = scope.getCurrentScopeName();

        LocalScope current = scope.getScope(this.getSymbol());
        for (int param = 0; param < this.params.size(); param++) {
            try {
                current.setValue(this.params.get(param).getSymbol(), callParams.params().get(param).evaluate(scope));
            } catch (IndexOutOfBoundsException e) {
                // Should never actually end up here
                throw new RuntimeException(e);
            }
        }
        scope.enableScope(this.getSymbol());
        TypedValue result = this.body.evaluate(scope);
        if (parentScope == null) {
            scope.clearScope();
        } else {
            scope.enableScope(parentScope);
        }
        return result;
    }

    @Override
    public List<Node> getChildren() {
        List<Node> children = new ArrayList<>();
        children.addAll(this.params);
        children.add(returnType);
        children.add(body);
        return children;
    }

    public Type getReturnType() {
        return this.returnType.getType();
    }

    @Override
    public String getSymbol() {
        return this.name;
    }
}