package internal.nodes;

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

    @Override
    public String convertToJott() {
        return type.toString();
    }

    @Override
    public boolean validateTree() {
        return false;
    }

    @Override
    public void execute() {

    }
}
