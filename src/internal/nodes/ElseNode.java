package internal.nodes;

import java.util.List;

import internal.ParseHaltException;
import internal.ParseUnexpectedTokenException;
import internal.PeekingArrayIterator;
import internal.scope.Scope;
import provided.TokenType;

/**
 * A return statement
 */
public class ElseNode extends Node {
    private final BodyNode body;

    public BodyNode getBody() {
        return body;
    }

    protected ElseNode(String filename, int lineNumber, BodyNode body) {
        super(filename, lineNumber);
        this.body = body;
        this.adopt();
    }

    public static ElseNode parse(PeekingArrayIterator it) throws ParseUnexpectedTokenException, ParseHaltException {
        int line = it.expect("Else").getLineNum();
        it.expect(TokenType.L_BRACE);
        BodyNode body = BodyNode.parse(it);
        it.expect(TokenType.R_BRACE);

        return new ElseNode(it.getCurrentFilename(), line, body);
    }

    @Override
    public String convertToJott() {
        return "Else{\n" +  this.body.convertToJott() + "}\n";
    }

    @Override
    public boolean validateTree(Scope scope) {
        return true;
    }

    @Override
    public void execute() {
    }

    @Override
    public List<Node> getChildren() {
        return List.of(body);
    }
}
