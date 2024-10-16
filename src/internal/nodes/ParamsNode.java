package internal.nodes;

import java.util.ArrayList;

import internal.ParseHaltException;
import internal.ParseUnexpectedTokenException;
import internal.PeekingArrayIterator;

public class ParamsNode extends Node{
    private final ArrayList<ExprNode> params;

    protected ParamsNode(int lineNumber, ArrayList<ExprNode> params) {
        super(lineNumber);
        this.params = params;
    }

    public static ParamsNode parse(PeekingArrayIterator it) throws ParseUnexpectedTokenException, ParseHaltException {
        ArrayList<ExprNode> params = new ArrayList<>();
        while (true) {
            if(it.peekExpectSafe("]") == 0) {
                return new ParamsNode(it.getCurrentLine(), params);
            } else {
                if (params.size() != 0) {
                    it.expect(",");
                }
                params.add(ExprNode.parse(it));
            }
        }
    }

    @Override
    public String convertToJott() {
        ArrayList<String> paramStrings = new ArrayList<>();
        for (ExprNode param : this.params) {
            paramStrings.add(param.convertToJott());

        }
        return String.join(",", paramStrings);

    }

    @Override
    public boolean validateTree() {
        return true;
    }

    @Override
    public void execute() {
    }
    
}
