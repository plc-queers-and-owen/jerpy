package internal.nodes;

import java.util.List;

import internal.ParseHaltException;
import internal.ParseUnexpectedTokenException;
import internal.PeekingArrayIterator;
import internal.scope.Scope;
import provided.TokenType;

/**
 * A function call operand
 */
public class FuncCallNode extends OperandNode {
    private final String name;
    private final ParamsNode params;

    protected FuncCallNode(int lineNumber, String name, ParamsNode params) {
        super(lineNumber);
        this.name = name;
        this.params = params;
        this.adopt();
    }

    // Needs completion
    public static FuncCallNode parse(PeekingArrayIterator it) throws ParseUnexpectedTokenException, ParseHaltException {
        int line = it.expect(TokenType.FC_HEADER).getLineNum();
        String name = it.expect(TokenType.ID_KEYWORD).getToken();
        it.expect(TokenType.L_BRACKET);
        ParamsNode params = ParamsNode.parse(it);
        it.expect("]");
        return new FuncCallNode(line, name, params);
    }

    @Override
    public String convertToJott() {
        return "::" + name + "[" + params.convertToJott() + "]";
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
        return List.of(this.params);
    }
}
