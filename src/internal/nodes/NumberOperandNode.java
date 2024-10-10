package internal.nodes;

import internal.ParseUnexpectedTokenException;
import internal.PeekingArrayIterator;
import internal.UnreachableException;
import provided.Token;
import provided.TokenType;

/**
 * A numerical constant operand
 */
public class NumberOperandNode extends OperandNode {
    private String val;

    protected NumberOperandNode(int lineNumber, String val) {
        super(lineNumber);
        this.val = val;
    }

    public static NumberOperandNode parse(PeekingArrayIterator it) throws ParseUnexpectedTokenException {
        switch (it.peekExpect("-", TokenType.NUMBER)) {
            case 0 -> {
                int line = it.peek().getLineNum();
                String val = "-" + it.expect(TokenType.NUMBER).getToken();
                it.skip();
                return new NumberOperandNode(line, val);
            }
            case 1 -> {
                Token t = it.peek();
                it.skip();
                return new NumberOperandNode(t.getLineNum(), t.getToken());
            }
            default -> throw new UnreachableException();
        }
    }

    @Override
    public String convertToJott() {
        return val;
    }

    @Override
    public boolean validateTree() {
        return true;
    }

    @Override
    public void execute() {
    }
}
