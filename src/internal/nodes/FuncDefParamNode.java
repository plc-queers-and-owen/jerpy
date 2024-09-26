package internal.nodes;

import internal.ParseError;
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
    }

    public FuncDefParamNode parse(PeekingArrayIterator it) throws ParseError {
        Token id = it.expect(TokenType.ID_KEYWORD);
        it.expect(TokenType.COLON);
        it.skip();
        TypeNode type = TypeNode.parse(it);
        return new FuncDefParamNode(id.getLineNum(), id.getToken(), type);
    }

    @Override
    public String convertToJott() {
        return id + ": " + type;
    }

    @Override
    public boolean validateTree() {
        return true;
    }

    @Override
    public void execute() {
    }
}
