package internal.nodes;

import internal.ParseError;
import internal.PeekingArrayIterator;
import provided.TokenType;

import java.util.ArrayList;
import java.util.StringJoiner;

/**
 * A function call operand
 */
public class FuncCallNode extends OperandNode {
    private final String name;
    private final ArrayList<ExprNode> params;

    protected FuncCallNode(int lineNumber, String name, ArrayList<ExprNode> params) {
        super(lineNumber);
        this.name = name;
        this.params = params;
    }

    // Needs completion
    public static FuncCallNode parse(PeekingArrayIterator it) throws ParseError {
        int line = it.expect(TokenType.FC_HEADER).getLineNum();
        String name = it.expect(TokenType.ID_KEYWORD).getToken();
        it.expect(TokenType.L_BRACKET);
    }

    @Override
    public String convertToJott() {
        StringJoiner joiner = new StringJoiner(", ");
        for (ExprNode expr : params) {
            joiner.add(expr.convertToJott());
        }
        return "::" + name + "(" + joiner + ")";
    }

    @Override
    public boolean validateTree() {
        return true;
    }

    @Override
    public void execute() {
    }
}
