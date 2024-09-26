package internal.nodes;

import internal.ParseError;
import internal.PeekingArrayIterator;
import internal.UnreachableException;
import provided.TokenType;

public abstract class OperandNode extends ExprNode {
    protected OperandNode(int lineNumber) {
        super(lineNumber);
    }

    public static OperandNode parse(PeekingArrayIterator it) throws ParseError {
        return switch (it.peekExpect(TokenType.NUMBER, "-", TokenType.FC_HEADER, TokenType.STRING, "True", "False", TokenType.ID_KEYWORD)) {
            case 0, 1 -> NumberOperandNode.parse(it);
            case 2 -> FuncCallNode.parse(it);
            case 3 -> StringOperandNode.parse(it);
            case 4, 5 -> BoolOperandNode.parse(it);
            case 6 -> IdOperandNode.parse(it);
            default -> throw new UnreachableException();
        };
    }
}
