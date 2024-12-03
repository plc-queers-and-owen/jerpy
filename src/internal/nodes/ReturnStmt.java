package internal.nodes;

import java.util.List;

import internal.ExecutionException;
import internal.ParseHaltException;
import internal.PeekingArrayIterator;
import internal.SemanticException;
import internal.SemanticReturnPathException;
import internal.SemanticTypeException;
import internal.eval.Type;
import internal.eval.TypedValue;
import internal.scope.Scope;
import provided.TokenType;

/**
 * A return statement
 */
public class ReturnStmt extends Node {
    private final ExprNode expr;

    protected ReturnStmt(String filename, int lineNumber, ExprNode expr) {
        super(filename, lineNumber);
        this.expr = expr;
        this.adopt();
    }

    public static ReturnStmt parse(PeekingArrayIterator it) throws ParseHaltException {
        int line = it.getCurrentLine();
        ExprNode expr = null;
        if (it.peekExpect("Return", TokenType.R_BRACE) == 0) {
            line = it.peek().getLineNum();
            it.skip();
            expr = ExprNode.parse(it);
            it.expect(TokenType.SEMICOLON);
        }
        return new ReturnStmt(it.getCurrentFilename(), line, expr);
    }

    @Override
    public String convertToJott() {
        if (expr == null) {
            return "";
        } else {
            return "Return " + expr.convertToJott() + ";\n";
        }
    }

    @Override
    public boolean validateTree(Scope scope) {
        if (this.getEnclosingFunction().getReturnType() == Type.Void) {
            new SemanticReturnPathException("Cannot return a value from a Void function.").report(this);
            return false;
        }

        try {
            if (this.getEnclosingFunction().getSymbol().equals("main")
                    && this.getEnclosingFunction().getReturnType() != Type.Void) {
                new SemanticTypeException(this.getEnclosingFunction().getReturnType(), Type.Void)
                        .report(this.getEnclosingFunction());
                return false;
            }
            if (this.getEnclosingFunction().getReturnType() != this.expr.inferType(scope)) {
                new SemanticTypeException(this.expr.inferType(scope), this.getEnclosingFunction().getReturnType())
                        .report(this);
                return false;
            }
        } catch (SemanticException e) {
            e.report(this);
            return false;
        }

        return true;
    }

    @Override
    public TypedValue evaluate(Scope scope) throws SemanticException, ExecutionException {
        return this.expr.evaluate(scope);
    }

    @Override
    public List<Node> getChildren() {
        return List.of(expr);
    }
}
