package internal.nodes;

import internal.ParseError;
import internal.PeekingArrayIterator;
import provided.TokenType;

public abstract class ExprNode extends Node {
    protected ExprNode(int lineNumber) {
        super(lineNumber);
    }

    public static ExprNode parse(PeekingArrayIterator it) throws ParseError {
        OperandNode base = OperandNode.parse(it);
        if (it.isDone()) {
            return base;
        } else if (it.peek().getTokenType() == TokenType.REL_OP || it.peek().getTokenType() == TokenType.MATH_OP) {
            return BiOpExprNode.parse(it, base);
        } else {
            return base;
        }
    }
}
