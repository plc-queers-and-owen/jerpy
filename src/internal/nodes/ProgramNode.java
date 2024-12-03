package internal.nodes;

import internal.ParseHaltException;
import internal.ParseUnexpectedTokenException;
import internal.PeekingArrayIterator;
import internal.SemanticUsageException;
import internal.scope.Scope;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * A Node representing a full Jott program
 */
public class ProgramNode extends Node {
    private final ArrayList<FunctionDefNode> functions;

    protected ProgramNode(String filename, int lineNumber, ArrayList<FunctionDefNode> functions) {
        super(filename, lineNumber);
        this.functions = functions;
        this.adopt();
    }

    public static ProgramNode parse(PeekingArrayIterator it) throws ParseUnexpectedTokenException, ParseHaltException {
        ArrayList<FunctionDefNode> functions = new ArrayList<>();
        while (!it.isDone()) {
            functions.add(FunctionDefNode.parse(it));
        }
        return new ProgramNode(it.getCurrentFilename(), 1, functions);
    }

    @Override
    public String convertToJott() {
        StringJoiner joiner = new StringJoiner("\n\n");
        for (FunctionDefNode function : functions) {
            joiner.add(function.convertToJott());
        }
        return joiner.toString();
    }

    @Override
    public boolean validateTree(Scope scope) {
        boolean result = functions.stream().allMatch(node -> node.validateTree(scope));
        if (!scope.isComplete("main") && result) {
            new SemanticUsageException("All programs must include a main[] function.").report(this);
            return false;
        }
        return result;
    }

    public void execute(Scope scope) {
    }

    @Override
    public List<Node> getChildren() {
        List<Node> children = new ArrayList<>();
        children.addAll(this.functions);
        return children;
    }
}
