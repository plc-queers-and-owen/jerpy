package internal.nodes;

import internal.ParseError;
import internal.PeekingArrayIterator;
import provided.Token;
import provided.TokenType;

/**
 * An identifier operand -- variable reference
 * Node: <id>
 */
public class IdOperandNode extends OperandNode {
    String id;

    protected IdOperandNode(int lineNumber, String id) {
        super(lineNumber);
        this.id = id;
    }

    public static IdOperandNode parse(PeekingArrayIterator it) throws ParseError {
        Token id = it.expect(TokenType.ID_KEYWORD);
        return new IdOperandNode(id.getLineNum(), id.getToken());
    }

    @Override
    public String convertToJott() {
        return id;
    }

    @Override
    public boolean validateTree() {
        return true;
    }

    @Override
    public void execute() {
    }
}
