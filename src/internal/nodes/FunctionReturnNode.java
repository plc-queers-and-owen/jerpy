package internal.nodes;

import java.util.List;

import internal.ParseUnexpectedTokenException;
import internal.PeekingArrayIterator;
import internal.eval.Type;
import internal.scope.Scope;

/**
 * The specified return type in a function definition
 */
public class FunctionReturnNode extends Node {
    private Type type;

    protected FunctionReturnNode(String filename, int lineNumber, Type type) {
        super(filename, lineNumber);
        this.type = type;
        this.adopt();
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

        return new FunctionReturnNode(it.getCurrentFilename(), lineNumber, type);
    }

    @Override
    public String convertToJott() {
        return type.toString();
    }

    @Override
    public boolean validateTree(Scope scope) {
        // System.out.println(this.convertToJott() + " :: " +
        // Integer.toString(this.getLineNumber()));
        return true;
    }

    @Override
    public List<Node> getChildren() {
        return List.of();
    }

    public Type getType() {
        return this.type;
    }
}
