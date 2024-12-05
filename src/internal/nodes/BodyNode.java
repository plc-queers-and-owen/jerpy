package internal.nodes;

import java.util.ArrayList;
import java.util.List;

import internal.ExecutionException;
import internal.ParseHaltException;
import internal.ParseUnexpectedTokenException;
import internal.PeekingArrayIterator;
import internal.SemanticException;
import internal.eval.TypedValue;
import internal.scope.Scope;

public class BodyNode extends Node {
    private final ArrayList<BodyStmtNode> bodyStatements;
    private final ReturnStmt returnStmt;

    protected BodyNode(String filename, int lineNumber, ArrayList<BodyStmtNode> bodyStatements, ReturnStmt returnStmt) {
        super(filename, lineNumber);
        this.bodyStatements = bodyStatements;
        this.returnStmt = returnStmt;
        this.adopt();
    }

    public static BodyNode parse(PeekingArrayIterator it) throws ParseUnexpectedTokenException, ParseHaltException {
        ArrayList<BodyStmtNode> statements = new ArrayList<>();
        while (true) {
            switch (it.peekExpectSafe("Return", ";", "}")) {
                case 0:
                    ReturnStmt returnStmt = ReturnStmt.parse(it);
                    it.peekExpect("}");
                    return new BodyNode(it.getCurrentFilename(), it.getCurrentLine(), statements, returnStmt);
                case 1:
                    it.skip();
                    break;
                case 2:
                    return new BodyNode(it.getCurrentFilename(), it.getCurrentLine(), statements, null);
                default:
                    statements.add(BodyStmtNode.parse(it));
            }
        }
    }

    @Override
    public String convertToJott() {
        String result = "";
        for (BodyStmtNode statement : this.bodyStatements) {
            result += statement.convertToJott() + "\n";
        }
        if (this.returnStmt != null) {
            result += this.returnStmt.convertToJott();
        }

        return result;
    }

    @Override
    public boolean validateTree(Scope scope) {
        // System.out.println(this.convertToJott() + " :: " +
        // Integer.toString(this.getLineNumber()));
        return this.bodyStatements.stream().allMatch(v -> v.validateTree(scope))
                && (this.returnStmt == null || this.returnStmt.validateTree(scope));
    }

    public boolean isReturnable() {
        if (returnStmt != null) return true;
        for (int i = bodyStatements.size() - 1; i >= 0; i--) {
            if (bodyStatements.get(i).isReturnable()) return true;
        }
        return false;
    }

    @Override
    public TypedValue evaluate(Scope scope) throws SemanticException, ExecutionException {
        for (BodyStmtNode stmt : this.bodyStatements) {
            TypedValue bodyReturn = stmt.evaluate(scope);
            if (bodyReturn.hasValue()) {
                return bodyReturn;
            }
        }

        if (this.returnStmt == null) {
            return new TypedValue();
        } else {
            return this.returnStmt.evaluate(scope);
        }
    }

    @Override
    public List<Node> getChildren() {
        List<Node> children = new ArrayList<Node>();
        children.addAll(this.bodyStatements);
        if (this.returnStmt != null) {
            children.add(returnStmt);
        }
        return children;
    }
}
