package internal.nodes;

import java.util.ArrayList;
import java.util.List;

import internal.ParseHaltException;
import internal.ParseUnexpectedTokenException;
import internal.PeekingArrayIterator;
import provided.TokenType;

/**
 * A return statement
 */
public class IfStmtNode extends Node {
    private final ExprNode expr;
    private final BodyNode body;
    private final ArrayList<ElseIfNode> elifs;
    private final ElseNode els;

    protected IfStmtNode(int lineNumber, ExprNode expr, BodyNode body, ArrayList<ElseIfNode> elseifs, ElseNode els) {
        super(lineNumber);
        this.expr = expr;
        this.body = body;
        this.elifs = elseifs;
        this.els = els;
        this.adopt();
    }

    public static IfStmtNode parse(PeekingArrayIterator it) throws ParseUnexpectedTokenException, ParseHaltException {
        int line = it.expect("If").getLineNum();
        it.expect(TokenType.L_BRACKET);
        ExprNode expr = ExprNode.parse(it);
        it.expect(TokenType.R_BRACKET);
        it.expect(TokenType.L_BRACE);
        BodyNode body = BodyNode.parse(it);
        it.expect(TokenType.R_BRACE);
        ArrayList<ElseIfNode> elifs = new ArrayList<ElseIfNode>();
        ElseNode els = null;
        while (it.peekExpectSafe("Elseif") >= 0) {
            ElseIfNode elif = ElseIfNode.parse(it);
            if (elif != null){
                elifs.add(elif);
            }
            else{
                break;
            }
        }
        if (it.peekExpectSafe("Else") >= 0) {
            els = ElseNode.parse(it);
        }

        return new IfStmtNode(line, expr, body, elifs, els);
    }

    @Override
    public String convertToJott() {
        String main = "If[" + this.expr.convertToJott() + "]{\n" + this.body.convertToJott() + "\n}\n";
        for (ElseIfNode elif : elifs) {
            main += elif.convertToJott();
        }
        
        if (this.els != null){
            main += this.els.convertToJott();
        }
        return main;
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
        List<Node> children = new ArrayList<>();
        children.add(expr);
        children.add(body);
        children.addAll(this.elifs);
        if (this.els != null) {
            children.add(this.els);
        }
        return children;
    }
}
