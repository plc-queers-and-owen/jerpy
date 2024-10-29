package internal.nodes;

import java.util.List;

import internal.ParseUnexpectedTokenException;
import internal.PeekingArrayIterator;
import internal.eval.Type;
import internal.scope.Scope;

/**
 * A Jott type as an ID_KEYWORD, not including "Void"
 */
public class TypeNode extends Node {
    private final Type type;

    protected TypeNode(String filename, int lineNumber, Type type) {
        super(filename, lineNumber);
        this.type = type;
        this.adopt();
    }

    public Type getType() {
        return type;
    }

    public static TypeNode parse(PeekingArrayIterator it) throws ParseUnexpectedTokenException {
        Type type = switch (it.peekExpect("Double", "Integer", "String", "Boolean")) {
            case 0 -> Type.Double;
            case 1 -> Type.Integer;
            case 2 -> Type.String;
            case 3 -> Type.Boolean;
            default -> null;
        };
        TypeNode ret = new TypeNode(it.getCurrentFilename(), it.peek().getLineNum(), type);
        it.skip();
        return ret;
    }

    @Override
    public String convertToJott() {
        return this.type.toString();
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
