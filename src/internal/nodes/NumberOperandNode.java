package internal.nodes;

import java.util.List;

import internal.ParseUnexpectedTokenException;
import internal.PeekingArrayIterator;
import internal.UnreachableException;
import internal.scope.Scope;
import provided.Token;
import provided.TokenType;

/**
 * A numerical constant operand
 */
public class NumberOperandNode extends OperandNode {
    private String val;

    protected NumberOperandNode(String filename, int lineNumber, String val) {
        super(filename, lineNumber);
        this.val = val;
        this.adopt();
    }

    public static NumberOperandNode parse(PeekingArrayIterator it) throws ParseUnexpectedTokenException {
        switch (it.peekExpect("-", TokenType.NUMBER)) {
            case 0 -> {
                int line = it.peek().getLineNum();
                String val = "-" + it.expect(TokenType.NUMBER).getToken();
                it.skip();
                return new NumberOperandNode(it.getCurrentFilename(), line, val);
            }
            case 1 -> {
                Token t = it.peek();
                it.skip();
                return new NumberOperandNode(it.getCurrentFilename(), t.getLineNum(), t.getToken());
            }
            default -> throw new UnreachableException();
        }
    }

    @Override
    public String convertToJott() {
        return val;
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
