package internal.nodes;

import internal.ParseHaltException;
import internal.ParseUnexpectedTokenException;
import internal.PeekingArrayIterator;

public class AsmtNode extends Node {
    private final IdOperandNode idNode;
    private final ExprNode expressionNode;

    protected AsmtNode(int lineNumber, IdOperandNode id, ExprNode expression) {
        super(lineNumber);
        this.idNode = id;
        this.expressionNode = expression;
    }

    @Override
    public String convertToJott() {
        return this.idNode.convertToJott() + " = " + this.expressionNode.convertToJott() + ";";
    }

    @Override
    public boolean validateTree() {
        return true;
    }

    @Override
    public void execute() {

    }

    public static AsmtNode parse(PeekingArrayIterator it) throws ParseHaltException, ParseUnexpectedTokenException {
        IdOperandNode id = IdOperandNode.parse(it);
        it.expect("=");
        ExprNode expression = ExprNode.parse(it);
        it.expect(";");
        return new AsmtNode(it.getCurrentLine(), id, expression);
    }
}
