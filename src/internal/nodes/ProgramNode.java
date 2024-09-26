package internal.nodes;

import internal.ParseError;
import internal.PeekingArrayIterator;
import provided.JottTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringJoiner;

public class ProgramNode extends Node {
    private final ArrayList<FunctionDefNode> functions;

    protected ProgramNode(int lineNumber, ArrayList<FunctionDefNode> functions) {
        super(lineNumber);
        this.functions = functions;
    }

    public static ProgramNode parse(PeekingArrayIterator it) throws ParseError {
        ArrayList<FunctionDefNode> functions = new ArrayList<>();
        while (!it.isDone()) {
            functions.add(FunctionDefNode.parse(it));
        }
        return new ProgramNode(1, functions);
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
    public boolean validateTree() {
        return functions.stream().allMatch(FunctionDefNode::validateTree);
    }

    @Override
    public void execute() {
    }
}
