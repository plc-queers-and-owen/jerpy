package internal.nodes;

import java.util.List;

import internal.ExecutionException;
import internal.ParseUnexpectedTokenException;
import internal.PeekingArrayIterator;
import internal.SemanticException;
import internal.SemanticNameException;
import internal.SemanticUninitializedVariableException;
import internal.eval.Type;
import internal.eval.TypedValue;
import internal.scope.Scope;
import provided.Token;
import provided.TokenType;

/**
 * An identifier operand -- variable reference
 * Node: <id>
 */
public class IdOperandNode extends OperandNode {
    String id;

    protected IdOperandNode(String filename, int lineNumber, String id) {
        super(filename, lineNumber);
        this.id = id;
        this.adopt();
    }

    public static IdOperandNode parse(PeekingArrayIterator it) throws ParseUnexpectedTokenException {
        Token id = it.expect(TokenType.ID_KEYWORD);
        return new IdOperandNode(it.getCurrentFilename(), id.getLineNum(), id.getToken());
    }

    @Override
    public String convertToJott() {
        return id;
    }

    @Override
    public boolean validateTree(Scope scope) {
        // System.out.println(this.convertToJott() + " :: " +
        // Integer.toString(this.getLineNumber()));
        if (validateId(id) && scope.getCurrentScope().isInitialized(id)) {
            return true;
        } else if (validateId(id)) {
            new SemanticUninitializedVariableException(id).report(this);
        } else {
            new SemanticNameException(id).report(this);
        }
        return false;
    }

    @Override
    public TypedValue evaluate(Scope scope) throws ExecutionException {
        try {
            return scope.getCurrentScope().getValue(id);
        } catch (SemanticException e) {
            throw new ExecutionException("Undeclared/uninitialized variable used.", this);
        }

    }

    @Override
    public List<Node> getChildren() {
        return List.of();
    }

    @Override
    public Type inferType(Scope scope) throws SemanticException {
        return scope.getCurrentScope().getType(id);
    }
}
