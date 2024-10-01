package internal.nodes;

import internal.ParseError;
import internal.PeekingArrayIterator;

/**
 * A boolean constant, True or False
 * Node: <bool>
 */
public class BoolOperandNode extends OperandNode {
    private final boolean val;

    protected BoolOperandNode(int lineNumber, boolean val) {
        super(lineNumber);
        this.val = val;
    }

    public static BoolOperandNode parse(PeekingArrayIterator it) throws ParseError {
        boolean val = it.peekExpect("True", "False") == 0;
        int line = it.peek().getLineNum();
        it.skip();
        return new BoolOperandNode(line, val);
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
