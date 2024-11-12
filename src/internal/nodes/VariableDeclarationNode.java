package internal.nodes;

import java.util.List;

import internal.ParseHaltException;
import internal.PeekingArrayIterator;
import internal.SemanticException;
import internal.SemanticNameException;
import internal.scope.Scope;
import internal.scope.VariableSymbol;
import provided.TokenType;

public class VariableDeclarationNode extends Node {

    TypeNode type;

    public TypeNode getType() {
        return type;
    }

    String name;

    public String getName() {
        return name;
    }

    protected VariableDeclarationNode(String filename, int lineNumber, TypeNode type, String name) {
        super(filename, lineNumber);
        this.type = type;
        this.name = name;
        this.adopt();
    }

    public static VariableDeclarationNode parse(PeekingArrayIterator it) throws ParseHaltException {
        TypeNode type = TypeNode.parse(it);

        String name = it.expect(TokenType.ID_KEYWORD).getToken();
        it.expect(";");

        return new VariableDeclarationNode(it.getCurrentFilename(), type.getLineNumber(), type, name);
    }

    @Override
    public String convertToJott() {
        return this.type.convertToJott() + " " + this.name + ';';
    }

    @Override
    public boolean validateTree(Scope scope) {
        if (!validateId(name)) {
            new SemanticNameException(name).report(this);
            return false;
        }

        if (this.type.validateTree(scope)) {
            try {
                scope.getCurrentScope().define(VariableSymbol.from(this));
                return true;
            } catch (SemanticException e) {
                e.report(this);
                return false;
            }
        }
        return false;
    }

    @Override
    public void execute() {
    }

    @Override
    public List<Node> getChildren() {
        return List.of(type);
    }

    @Override
    public String getSymbol() {
        return this.name;
    }

}