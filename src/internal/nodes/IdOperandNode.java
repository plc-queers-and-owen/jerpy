package internal.nodes;

import java.util.List;

import internal.ParseUnexpectedTokenException;
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
        this.adopt();
    }

    public static IdOperandNode parse(PeekingArrayIterator it) throws ParseUnexpectedTokenException {
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

    @Override
    public List<Node> getChildren() {
        return List.of();
    }
}
