package internal.nodes;

import java.util.List;

import internal.ParseHaltException;
import internal.PeekingArrayIterator;
import provided.TokenType;

public class VariableDeclarationNode extends Node {

    TypeNode type;
    String name;

    protected VariableDeclarationNode(int lineNumber, TypeNode type, String name) {
        super(lineNumber);
        this.type = type;
        this.name = name;
        this.adopt();
    }

    public static VariableDeclarationNode parse(PeekingArrayIterator it) throws ParseHaltException {
        TypeNode type = TypeNode.parse(it);

        String name = it.expect(TokenType.ID_KEYWORD).getToken();

        return new VariableDeclarationNode(type.getLineNumber(), type, name);
    }

    @Override
    public String convertToJott() {
        return this.type.convertToJott() + " " + this.name + ';';
    }

    @Override
    public boolean validateTree() {
        return true;
    }

    @Override
    public void execute() {
    }

    @Override
    public List<Node> getChildren() {
        return List.of(type);
    }

}