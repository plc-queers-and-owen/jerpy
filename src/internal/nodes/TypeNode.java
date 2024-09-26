package internal.nodes;

import internal.ParseError;
import internal.PeekingArrayIterator;
import internal.eval.Type;

/**
 * A Jott type as an ID_KEYWORD, not including "Void"
 */
public class TypeNode extends Node {
    private final Type type;

    protected TypeNode(int lineNumber, Type type) {
        super(lineNumber);
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public static TypeNode parse(PeekingArrayIterator it) throws ParseError {
        Type type = switch (it.peekExpect("Double", "Integer", "String", "Boolean")) {
            case 0 -> Type.Double;
            case 1 -> Type.Integer;
            case 2 -> Type.String;
            case 3 -> Type.Boolean;
            default -> null;
        };
        TypeNode ret = new TypeNode(it.peek().getLineNum(), type);
        it.skip();
        return ret;
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
