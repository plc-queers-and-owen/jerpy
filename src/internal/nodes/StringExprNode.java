package internal.nodes;

import internal.ParseUnexpectedTokenException;
import internal.PeekingArrayIterator;
import provided.Token;
import provided.TokenType;

/**
 * An operand representing a string literal
 */
public class StringExprNode extends ExprNode {
    private final String val;

    protected StringExprNode(int lineNumber, String val) {
        super(lineNumber);
        this.val = val;
    }

    public static StringExprNode parse(PeekingArrayIterator it) throws ParseUnexpectedTokenException {
        Token tk = it.expect(TokenType.STRING);
        return new StringExprNode(tk.getLineNum(), tk.getToken());
    }

    @Override
    public String convertToJott() {
        return "\"" + val + "\""; // no need to escape anything
    }

    @Override
    public boolean validateTree() {
        return true;
    }

    @Override
    public void execute() {
    }
}
