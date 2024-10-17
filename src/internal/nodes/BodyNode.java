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
                    return new BodyNode(it.getCurrentLine(), statements, ReturnStmt.parse(it));
                case 1:
                    it.skip();
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

        result += this.returnStmt.convertToJott();
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
