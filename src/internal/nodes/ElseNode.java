package internal.nodes;

import internal.ParseHaltException;
import internal.ParseUnexpectedTokenException;
import internal.PeekingArrayIterator;
import provided.TokenType;

/**
 * A return statement
 */
public class ElseNode extends Node {
    private final BodyNode body;

    protected ElseNode(int lineNumber, BodyNode body) {
        super(lineNumber);
        this.body = body;
    }

    public static ElseNode parse(PeekingArrayIterator it) throws ParseUnexpectedTokenException, ParseHaltException {
        int line = it.expect("Else").getLineNum();
        it.expect(TokenType.L_BRACE);
        BodyNode body = BodyNode.parse(it);
        it.expect(TokenType.R_BRACE);

        return new ElseNode(line, body);
    }

    @Override
    public String convertToJott() {
        return "Else{\n" +  this.body.convertToJott() + "}\n";
    }

    @Override
    public boolean validateTree() {
        return true;
    }

    @Override
    public void execute() {
    }
}
