package internal.nodes;

import java.util.List;

import internal.ParseUnexpectedTokenException;
import internal.PeekingArrayIterator;
import internal.SemanticException;
import internal.eval.Type;
import internal.eval.TypedValue;
import internal.scope.Scope;

/**
 * A boolean constant, True or False
 * Node: <bool>
 */
public class BoolExprNode extends ExprNode {
    private final boolean val;

    protected BoolExprNode(String filename, int lineNumber, boolean val) {
        super(filename, lineNumber);
        this.val = val;
        this.adopt();
    }

    public static BoolExprNode parse(PeekingArrayIterator it) throws ParseUnexpectedTokenException {
        boolean val = it.peekExpect("True", "False") == 0;
        int line = it.peek().getLineNum();
        it.skip();
        return new BoolExprNode(it.getCurrentFilename(), line, val);
    }

    @Override
    public String convertToJott() {
        return val ? "True" : "False";
    }

    @Override
    public boolean validateTree(Scope scope) {
        return true;
    }

    @Override
    public TypedValue evaluate(Scope scope) {
        return new TypedValue(val);
    }

    @Override
    public List<Node> getChildren() {
        return List.of();
    }

    @Override
    public Type inferType(Scope scope) throws SemanticException {
        return Type.Boolean;
    }
}
