package internal.nodes;

import internal.ParseError;
import internal.PeekingArrayIterator;
import provided.TokenType;

public class ReturnStmt extends Node {
    private final ExprNode expr;

    protected ReturnStmt(int lineNumber, ExprNode expr) {
        super(lineNumber);
        this.expr = expr;
    }

    public static ReturnStmt parse(PeekingArrayIterator it) throws ParseError {
        int line = it.expect("Return").getLineNum();
        ExprNode expr = ExprNode.parse(it);

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
        return "Return " + expr.convertToJott() + ";";
    }

    @Override
    public boolean validateTree() {
        return true;
    }

    @Override
    public void execute() {
    }
}
