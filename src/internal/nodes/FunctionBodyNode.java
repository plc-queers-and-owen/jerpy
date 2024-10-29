package internal.nodes;

import java.util.ArrayList;
import java.util.List;

import internal.ParseHaltException;
import internal.PeekingArrayIterator;
import internal.eval.Type;
import internal.scope.Scope;

/**
 * The body of a function definition
 */
public class FunctionBodyNode extends Node {

    List<VariableDeclarationNode> varDeclarations;
    BodyNode body;

    protected FunctionBodyNode(int lineNumber, List<VariableDeclarationNode> varDeclarations, BodyNode body) {
        super(lineNumber);
        this.varDeclarations = varDeclarations;
        this.body = body;
        this.adopt();
    }

    public static FunctionBodyNode parse(PeekingArrayIterator it) throws ParseHaltException {
        // line number is a weird thing to set here but
        // we just set it equal to the line number of the first token
        // parsed.
        // Might make more sense if we had a full span system but whatever
        int lineNumber = -1;

        ArrayList<VariableDeclarationNode> varDeclarations = new ArrayList<>();

        // Since no statements can begin with a type we can check if the next token is a
        // type and know we still have variable declarations to parse
        while (Type.isVarType(it.peek().getToken())) {
            if (lineNumber == -1) {
                lineNumber = it.peek().getLineNum();
            }

            varDeclarations.add(VariableDeclarationNode.parse(it));
        }

        BodyNode body = BodyNode.parse(it);
        if (lineNumber == -1) {
            lineNumber = body.getLineNumber();
        }

        return new FunctionBodyNode(lineNumber, varDeclarations, body);

    }

    @Override
    public String convertToJott() {
        StringBuilder output = new StringBuilder();

        for (VariableDeclarationNode var : this.varDeclarations) {
            output.append(var.convertToJott());
            output.append('\n');
        }

        output.append(this.body.convertToJott());

        return output.toString();
    }

    @Override
    public boolean validateTree(Scope scope) {
        return true;
    }

    @Override
    public void execute() {
    }

    @Override
    public List<Node> getChildren() {
        List<Node> children = new ArrayList<>();
        children.addAll(this.varDeclarations);
        children.add(body);
        return children;
    }
}
