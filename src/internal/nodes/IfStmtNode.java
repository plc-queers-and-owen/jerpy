package internal.nodes;

import internal.ParseError;
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

    public static IfStmtNode parse(PeekingArrayIterator it) throws ParseError {
        int line = it.expect("If").getLineNum();
        it.expect(TOKEN.L_BRACKET);
        ExprNode expr = ExprNode.parse(it);
        it.expect(TOKEN.R_BRACKET);
        it.expect(TOKEN.L_BRACE);
        BodyNode body = BodyNode.parse(it);
        it.expect(TOKEN.R_BRACE);
        ArrayList<ElseIfNode> elifs = new ArrayList<ElseIfNode>();
        ElseNode els = null;
        while (it.peekExpect("Elseif")){
            ElseIfNode elif = ElseIfNode.parse(it);
            if (elif != null){
                elifs.add(elif);
            }
            else{
                break;
            }
        }
        if (it.peekExpect("Else")){
            els = ElseNode.parse(it);
        }

        return new IfStmtNode(line, expr, body, elifs, els);
    }

    @Override
    public String convertToJott() {
        String main = "If[" + this.expr.convertToJott() + "]{\n" + this.body.convertToJott() + "\n}\n";
        this.elifs.foreach((elif) -> { main += elif.convertToJott(); });
        
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
