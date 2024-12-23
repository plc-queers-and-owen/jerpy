package internal.nodes;

import java.util.List;

import internal.ExecutionException;
import internal.ParseHaltException;
import internal.ParseUnexpectedTokenException;
import internal.PeekingArrayIterator;
import internal.SemanticException;
import internal.SemanticTypeException;
import internal.eval.TypedValue;
import internal.scope.Scope;

public class AsmtNode extends Node {
    private final IdOperandNode idNode;
    private final ExprNode expressionNode;

    protected AsmtNode(String filename, int lineNumber, IdOperandNode id, ExprNode expression) {
        super(filename, lineNumber);
        this.idNode = id;
        this.expressionNode = expression;
        this.adopt();
    }

    @Override
    public String convertToJott() {
        return this.idNode.convertToJott() + " = " + this.expressionNode.convertToJott() + ";";
    }

    @Override
    public boolean validateTree(Scope scope) {
        // System.out.println(this.convertToJott() + " :: " +
        // Integer.toString(this.getLineNumber()));
        try {
            if (validateId(this.idNode.id) && this.expressionNode.validateTree(scope)) {
                if (scope.getCurrentScope().getType(this.idNode.id).equals(this.expressionNode.inferType(scope))) {
                    scope.getCurrentScope().initialize(this.idNode.id);
                    return true;
                } else {
                    new SemanticTypeException(this.expressionNode.inferType(scope),
                            scope.getCurrentScope().getType(this.idNode.id)).report(this);
                    return false;
                }
            }
        } catch (SemanticException e) {
            e.report(this);
            return false;
        }

        return false;
    }

    @Override
    public TypedValue evaluate(Scope scope) throws SemanticException, ExecutionException {
        scope.getCurrentScope().setValue(this.idNode.id, this.expressionNode.evaluate(scope));
        return new TypedValue();
    }

    public static AsmtNode parse(PeekingArrayIterator it) throws ParseHaltException, ParseUnexpectedTokenException {
        IdOperandNode id = IdOperandNode.parse(it);
        it.expect("=");
        ExprNode expression = ExprNode.parse(it);
        it.expect(";");
        return new AsmtNode(it.getCurrentFilename(), it.getCurrentLine(), id, expression);
    }

    @Override
    public List<Node> getChildren() {
        return List.of(this.idNode, this.expressionNode);
    }
}
