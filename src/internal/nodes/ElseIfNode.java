package internal.nodes;

import internal.ParseError;
import internal.PeekingArrayIterator;
import provided.TokenType;

/**
 * A return statement
 */
public class ElseIfNode extends Node {
    private final ExprNode expr;
    private final BodyNode body;

    protected ElseIfNode(int lineNumber, ExprNode expr, BodyNode body) {
        super(lineNumber);
        this.expr = expr;
        this.body = body;
    }

    public static ElseIfNode parse(PeekingArrayIterator it) throws ParseError {
        int line = it.expect("Elseif").getLineNum();
        it.expect(TOKEN.L_BRACKET);
        ExprNode expr = ExprNode.parse(it);
        it.expect(TOKEN.R_BRACKET);
        it.expect(TOKEN.L_BRACE);
        BodyNode body = BodyNode.parse(it);
        it.expect(TOKEN.R_BRACE);

        return new ElseIfNode(line, expr, body);
    }

    @Override
    public String convertToJott() {
        return "Elseif[" + this.expr.convertToJott() + "]{\n" + this.body.convertToJott() + "\n}\n";
    }

    @Override
    public boolean validateTree() {
        return true;
    }

    @Override
    public void execute() {
    }
}
