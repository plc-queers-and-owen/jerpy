package internal.nodes;

import internal.ParseError;
import internal.PeekingArrayIterator;
import provided.TokenType;

/**
 * A return statement
 */
public class ElseNode extends Node {
    private final ExprNode expr;
    private final BodyNode body;

    protected ElseNode(int lineNumber, BodyNode body) {
        super(lineNumber);
        this.body = body;
    }

    public static ElseNode parse(PeekingArrayIterator it) throws ParseError {
        int line = it.expect("Else").getLineNum();
        it.expect(TOKEN.L_BRACE);
        BodyNode body = BodyNode.parse(it);
        it.expect(TOKEN.R_BRACE);

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
