package internal.nodes;

import java.util.ArrayList;
import java.util.List;

import internal.*;
import internal.eval.TypedValue;
import internal.scope.Scope;
import provided.TokenType;

/**
 * A return statement
 */
public class IfStmtNode extends Node {
    private final ExprNode expr;
    private final BodyNode body;

    public BodyNode getBody() {
        return body;
    }

    private final ArrayList<ElseIfNode> elifs;

    public ArrayList<ElseIfNode> getElifs() {
        return elifs;
    }

    private final ElseNode els;

    public ElseNode getEls() {
        return els;
    }

    protected IfStmtNode(String filename, int lineNumber, ExprNode expr, BodyNode body, ArrayList<ElseIfNode> elseifs,
            ElseNode els) {
        super(filename, lineNumber);
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

        return new IfStmtNode(it.getCurrentFilename(), line, expr, body, elifs, els);
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
    public boolean validateTree(Scope scope) {
        // System.out.println(this.convertToJott() + " :: " +
        // Integer.toString(this.getLineNumber()));
        return this.expr.validateTree(scope) && this.body.validateTree(scope)
                && this.elifs.stream().allMatch(v -> v.validateTree(scope))
                && (this.els == null || this.els.validateTree(scope));
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

    @Override
    public TypedValue evaluate(Scope scope) throws SemanticException, ExecutionException, ReturnException {
        if (this.expr.evaluate(scope).getBoolean()) {
            return this.body.evaluate(scope);
        }

        for (ElseIfNode elif : this.elifs) {
            if (elif.matches(scope)) {
                return elif.getBody().evaluate(scope);
            }
        }

        if (this.els == null) {
            return new TypedValue();
        } else {
            return this.els.evaluate(scope);
        }
    }

    public boolean isReturnable() {
        if (els == null) return false;
        return body.isReturnable() && els.getBody().isReturnable() && elifs.stream().allMatch(x -> x.getBody().isReturnable());
    }
}
