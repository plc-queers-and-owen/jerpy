package internal.nodes;

import java.util.List;

import internal.ParseHaltException;
import internal.PeekingArrayIterator;
import internal.scope.Scope;
import provided.TokenType;

/**
 * A return statement
 */
public class ReturnStmt extends Node {
    private final ExprNode expr;

    protected ReturnStmt(String filename, int lineNumber, ExprNode expr) {
        super(filename, lineNumber);
        this.expr = expr;
        this.adopt();
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
        return new ReturnStmt(it.getCurrentFilename(), line, expr);
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
    public boolean validateTree(Scope scope) {
        return true;
    }

    @Override
    public void execute() {
    }

    @Override
    public List<Node> getChildren() {
        return List.of(expr);
    }
}
