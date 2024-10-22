package internal.nodes;

import java.util.List;

import internal.ParseHaltException;
import internal.ParseUnexpectedTokenException;
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
        this.adopt();
    }

    public static ElseIfNode parse(PeekingArrayIterator it) throws ParseUnexpectedTokenException, ParseHaltException {
        int line = it.expect("Elseif").getLineNum();
        it.expect(TokenType.L_BRACKET);
        ExprNode expr = ExprNode.parse(it);
        it.expect(TokenType.R_BRACKET);
        it.expect(TokenType.L_BRACE);
        BodyNode body = BodyNode.parse(it);
        it.expect(TokenType.R_BRACE);

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

    @Override
    public List<Node> getChildren() {
        return List.of(this.expr, this.body);
    }
}
