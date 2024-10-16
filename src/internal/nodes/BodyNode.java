package internal.nodes;

import java.util.ArrayList;

import internal.ParseHaltException;
import internal.ParseUnexpectedTokenException;
import internal.PeekingArrayIterator;

public class BodyNode extends Node {
    private final ArrayList<BodyStmtNode> bodyStatements;
    private final ReturnStmt returnStmt;

    protected BodyNode(int lineNumber, ArrayList<BodyStmtNode> bodyStatements, ReturnStmt returnStmt) {
        super(lineNumber);
        this.bodyStatements = bodyStatements;
        this.returnStmt = returnStmt;
    }

    public static BodyNode parse(PeekingArrayIterator it) throws ParseUnexpectedTokenException, ParseHaltException {
        ArrayList<BodyStmtNode> statements = new ArrayList<>();
        while (true) {
            switch (it.peekExpectSafe("Return", ";", "}")) {
                case 0:
                    ReturnStmt returnStmt = ReturnStmt.parse(it);
                    it.peekExpect("}");
                    return new BodyNode(it.getCurrentLine(), statements, returnStmt);
                case 1:
                    it.skip();
                    break;
                case 2:
                    return new BodyNode(it.getCurrentLine(), statements, null);
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
    public boolean validateTree() {
        return true;
    }

    @Override
    public void execute() {
    }
}
