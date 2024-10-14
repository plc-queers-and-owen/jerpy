package internal.nodes;

import internal.ParseHaltException;
import internal.ParseUnexpectedTokenException;
import internal.PeekingArrayIterator;

public class WhileLoopNode extends Node {
    private final ExprNode expression;
    private final BodyNode body;

    protected WhileLoopNode(int lineNumber, ExprNode expression, BodyNode body) {
        super(lineNumber);
        this.expression = expression;
        this.body = body;
    }

    @Override
    public String convertToJott() {
        return "While [" + this.expression.convertToJott() + "]{\n" + this.body.convertToJott() + "}\n";
    }

    @Override
    public boolean validateTree() {
        return true;
    }

    @Override
    public void execute() {

    }

    public static WhileLoopNode parse(PeekingArrayIterator it)
            throws ParseHaltException, ParseUnexpectedTokenException {
        it.expect("While");
        it.expect("[");
        ExprNode expression = ExprNode.parse(it);
        it.expect("]");
        it.expect("{");
        BodyNode body = BodyNode.parse(it);
        it.expect("}");
        return new WhileLoopNode(it.getCurrentLine(), expression, body);
    }

}
