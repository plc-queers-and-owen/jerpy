package internal.nodes;

import internal.BodyStmtType;
import internal.ParseHaltException;
import internal.ParseUnexpectedTokenException;
import internal.PeekingArrayIterator;
import provided.TokenType;

public class BodyStmtNode extends Node {
    private final FuncCallNode funcCall;
    private final IfStmtNode ifStmt;
    private final WhileLoopNode whileLoop;
    private final AsmtNode asmt;

    protected BodyStmtNode(int lineNumber, FuncCallNode contained) {
        super(lineNumber);
        this.funcCall = contained;
        this.ifStmt = null;
        this.whileLoop = null;
        this.asmt = null;
    }

    protected BodyStmtNode(int lineNumber, IfStmtNode contained) {
        super(lineNumber);
        this.funcCall = null;
        this.ifStmt = contained;
        this.whileLoop = null;
        this.asmt = null;
    }

    protected BodyStmtNode(int lineNumber, WhileLoopNode contained) {
        super(lineNumber);
        this.funcCall = null;
        this.ifStmt = null;
        this.whileLoop = contained;
        this.asmt = null;
    }

    protected BodyStmtNode(int lineNumber, AsmtNode contained) {
        super(lineNumber);
        this.funcCall = null;
        this.ifStmt = null;
        this.whileLoop = null;
        this.asmt = contained;
    }

    /**
     * Gets the type of the contained node
     * 
     * @return The type of the contained node
     */
    public BodyStmtType getType() {
        if (this.funcCall != null) {
            return BodyStmtType.FUNC_CALL;
        }

        if (this.ifStmt != null) {
            return BodyStmtType.IF;
        }

        if (this.whileLoop != null) {
            return BodyStmtType.WHILE;
        }

        return BodyStmtType.ASMT;
    }

    @Override
    public void execute() {

    }

    @Override
    public boolean validateTree() {
        return true;
    }

    public static BodyStmtNode parse(PeekingArrayIterator it) throws ParseUnexpectedTokenException, ParseHaltException {
        switch (it.peekExpect(TokenType.FC_HEADER, "While", "If", TokenType.ID_KEYWORD)) {
            case 0:
                return new BodyStmtNode(it.getCurrentLine(), FuncCallNode.parse(it));
            case 1:
                return new BodyStmtNode(it.getCurrentLine(), WhileLoopNode.parse(it));
            case 2:
                return new BodyStmtNode(it.getCurrentLine(), IfStmtNode.parse(it));
            default:
                return new BodyStmtNode(it.getCurrentLine(), AsmtNode.parse(it));
        }
    }

    @Override
    public String convertToJott() {
        switch (this.getType()) {
            case BodyStmtType.FUNC_CALL:
                return this.funcCall.convertToJott();
            case BodyStmtType.IF:
                return this.ifStmt.convertToJott();
            case BodyStmtType.WHILE:
                return this.whileLoop.convertToJott();
            default:
                return this.asmt.convertToJott();
        }
    }
}
