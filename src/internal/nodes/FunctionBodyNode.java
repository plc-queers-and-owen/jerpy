package internal.nodes;

import java.util.ArrayList;
import java.util.List;

import internal.ExecutionException;
import internal.ParseHaltException;
import internal.PeekingArrayIterator;
import internal.SemanticException;
import internal.SemanticReturnPathException;
import internal.eval.Type;
import internal.eval.TypedValue;
import internal.scope.Scope;

/**
 * The body of a function definition
 */
public class FunctionBodyNode extends Node {

    List<VariableDeclarationNode> varDeclarations;
    BodyNode body;

    protected FunctionBodyNode(String filename, int lineNumber, List<VariableDeclarationNode> varDeclarations,
            BodyNode body) {
        super(filename, lineNumber);
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

        return new FunctionBodyNode(it.getCurrentFilename(), lineNumber, varDeclarations, body);

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
        for (VariableDeclarationNode dec : varDeclarations) {
            if (!dec.validateTree(scope)) {
                return false;
            }
        }

        if (this.body.validateTree(scope)) {
            if (this.getEnclosingFunction().getReturnType() != Type.Void && !this.body.isReturnable()) {
                new SemanticReturnPathException("Not all code paths return a value.")
                        .report(this.getEnclosingFunction());
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public TypedValue evaluate(Scope scope) throws SemanticException, ExecutionException {
        return null;
    }

    @Override
    public List<Node> getChildren() {
        List<Node> children = new ArrayList<>();
        children.addAll(this.varDeclarations);
        children.add(body);
        return children;
    }
}
