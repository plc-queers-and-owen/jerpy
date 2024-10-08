package internal.nodes;

import internal.ParseUnexpectedTokenException;
import internal.PeekingArrayIterator;

/**
 * A boolean constant, True or False
 * Node: <bool>
 */
public class BoolExprNode extends ExprNode {
    private final boolean val;

    protected BoolExprNode(int lineNumber, boolean val) {
        super(lineNumber);
        this.val = val;
    }

    public static BoolExprNode parse(PeekingArrayIterator it) throws ParseUnexpectedTokenException {
        boolean val = it.peekExpect("True", "False") == 0;
        int line = it.peek().getLineNum();
        it.skip();
        return new BoolExprNode(line, val);
    }

    @Override
    public String convertToJott() {
        return val ? "True" : "False";
    }

    @Override
    public boolean validateTree() {
        return true;
    }

    @Override
    public void execute() {
    }
}
