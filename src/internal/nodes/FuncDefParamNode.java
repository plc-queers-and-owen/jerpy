package internal.nodes;

import java.util.List;

import internal.ParseUnexpectedTokenException;
import internal.PeekingArrayIterator;
import provided.Token;
import provided.TokenType;

/**
 * A parameter used as part of a function definition
 */
public class FuncDefParamNode extends Node {
    private final String id;
    private final TypeNode type;

    protected FuncDefParamNode(int lineNumber, String id, TypeNode type) {
        super(lineNumber);
        this.id = id;
        this.type = type;
        this.adopt();
    }

    public static FuncDefParamNode parse(PeekingArrayIterator it) throws ParseUnexpectedTokenException {
        Token id = it.expect(TokenType.ID_KEYWORD);
        // Looks like we're skipping twice here?
        it.expect(TokenType.COLON);
        TypeNode type = TypeNode.parse(it);
        return new FuncDefParamNode(id.getLineNum(), id.getToken(), type);
    }

    @Override
    public String convertToJott() {
        return id + ": " + type.convertToJott();
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
        return List.of(type);
    }
}
