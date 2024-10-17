package internal.nodes;

import internal.ParseUnexpectedTokenException;
import internal.PeekingArrayIterator;
import internal.eval.Type;

/**
 * The specified return type in a function definition
 */
public class FunctionReturnNode extends Node {
    private Type type;

    protected FunctionReturnNode(int lineNumber, Type type) {
        super(lineNumber);
        this.type = type;
    }

    public static FunctionReturnNode parse(PeekingArrayIterator it) throws ParseUnexpectedTokenException {
        int lineNumber = it.getCurrentLine();
        Type type = Type.fromString(it.peek().getToken());

        // If we get null we don't have a valid type so we error
        if (type == null) {
            throw new ParseUnexpectedTokenException(
                    new String[] { "String", "Boolean", "Integer", "Double", "Void" },
                    it.peek().getToken(), it.getContext());
        }

        // Skip since we only peeked earlier
        it.skip();

        return new FunctionReturnNode(lineNumber, type);
    }

    @Override
    public String convertToJott() {
        return type.toString();
    }

    @Override
    public boolean validateTree() {
        return true;
    }

    @Override
    public void execute() {

    }
}
