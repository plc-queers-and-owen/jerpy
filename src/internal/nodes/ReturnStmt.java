package internal.nodes;

import internal.ParseHaltException;
import internal.PeekingArrayIterator;
import provided.TokenType;

/**
 * A return statement
 */
public class ReturnStmt extends Node {
    private final ExprNode expr;

    protected ReturnStmt(int lineNumber, ExprNode expr) {
        super(lineNumber);
        this.expr = expr;
    }

    public static ReturnStmt parse(PeekingArrayIterator it) throws ParseHaltException {
        int line = it.getCurrentLine();
        ExprNode expr = null;
        if (it.peekExpect("Return", TokenType.R_BRACE) == 0) {
            line = it.peek().getLineNum();
            it.skip();
            expr = ExprNode.parse(it);
            it.expect(TokenType.SEMICOLON);
        }
        return new ReturnStmt(line, expr);
    }

    @Override
    public String convertToJott() {
        if (expr == null) {
            return "";
        } else {
            return "Return " + expr.convertToJott() + ";\n";
        }
    }

    @Override
    public boolean validateTree() {
        return true;
    }

    @Override
    public void execute() {
    }
}
