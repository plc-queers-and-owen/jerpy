package internal.nodes;

import java.util.ArrayList;

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
    }

    public static IfStmtNode parse(PeekingArrayIterator it) throws ParseUnexpectedTokenException {
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
        this.elifs.forEach((elif) -> {
            main += elif.convertToJott();
        });
        
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
}
