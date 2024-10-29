package internal.nodes;

import java.util.List;

import internal.ParseUnexpectedTokenException;
import internal.PeekingArrayIterator;
import internal.scope.Scope;
import provided.Token;
import provided.TokenType;

/**
 * An operand representing a string literal
 */
public class StringExprNode extends ExprNode {
    private final String val;

    protected StringExprNode(String filename, int lineNumber, String val) {
        super(filename, lineNumber);
        this.val = val;
        this.adopt();
    }

    public static StringExprNode parse(PeekingArrayIterator it) throws ParseUnexpectedTokenException {
        Token tk = it.expect(TokenType.STRING);
        return new StringExprNode(it.getCurrentFilename(), tk.getLineNum(), tk.getToken());
    }

    @Override
    public String convertToJott() {
        return val; // no need to escape anything
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
        return List.of();
    }
}
