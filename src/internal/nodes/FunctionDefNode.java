package internal.nodes;

import java.util.ArrayList;
import java.util.List;

import internal.ParseHaltException;
import internal.PeekingArrayIterator;
import internal.scope.Scope;
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
        return true;
    }

    @Override
    public void execute() {
    }

    @Override
    public List<Node> getChildren() {
        List<Node> children = new ArrayList<>();
        children.addAll(this.params);
        children.add(returnType);
        children.add(body);
        return children;
    }
}