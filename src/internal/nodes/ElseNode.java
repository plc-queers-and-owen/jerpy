package internal.nodes;

import java.util.List;

import internal.ExecutionException;
import internal.ParseHaltException;
import internal.ParseUnexpectedTokenException;
import internal.PeekingArrayIterator;
import internal.SemanticException;
import internal.eval.TypedValue;
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
        // System.out.println(this.convertToJott() + " :: " +
        // Integer.toString(this.getLineNumber()));
        return this.body.validateTree(scope);
    }

    @Override
    public List<Node> getChildren() {
        return List.of(body);
    }

    @Override
    public TypedValue evaluate(Scope scope) throws SemanticException, ExecutionException {
        return this.body.evaluate(scope);
    }
}
