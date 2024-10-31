package internal.nodes;

import internal.ParseHaltException;
import internal.ParseUnexpectedTokenException;
import internal.PeekingArrayIterator;
import internal.SemanticException;
import internal.UnreachableException;
import internal.eval.Type;
import internal.scope.Scope;
import provided.TokenType;

/**
 * An abstract base class representing an operand
 */
public abstract class OperandNode extends ExprNode {
    protected OperandNode(String filename, int lineNumber) {
        super(filename, lineNumber);
    }

    public static OperandNode parse(PeekingArrayIterator it) throws ParseUnexpectedTokenException, ParseHaltException {
        return switch (it.peekExpect(TokenType.NUMBER, "-", TokenType.FC_HEADER, TokenType.ID_KEYWORD)) {
            case 0, 1 -> NumberOperandNode.parse(it);
            case 2 -> FuncCallNode.parse(it);
            case 3 -> IdOperandNode.parse(it);
            default -> throw new UnreachableException();
        };
    }

    public abstract Type inferType(Scope scope) throws SemanticException;
}
