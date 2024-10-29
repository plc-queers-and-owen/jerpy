package internal.nodes;

import internal.ParseHaltException;
import internal.ParseLowerCaseIdentifierException;
import internal.PeekingArrayIterator;
import provided.TokenType;

/**
 * An abstract base class for Jott expressions
 */
public abstract class ExprNode extends Node {
    protected ExprNode(String filename, int lineNumber) {
        super(filename, lineNumber);
    }

    public static ExprNode parse(PeekingArrayIterator it) throws ParseHaltException {
        switch (it.peekExpect(TokenType.ID_KEYWORD, TokenType.NUMBER, "-", TokenType.FC_HEADER, TokenType.STRING)) {
            case 0 -> {
                String data = it.peek().getToken();
                if (data.equals("True") || data.equals("False")) {
                    return BoolExprNode.parse(it);
                } else if (Character.isUpperCase(data.charAt(0))) {
                    throw new ParseLowerCaseIdentifierException();
                }
            }
            case 4 -> {
                return StringExprNode.parse(it);
            }
        }
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
