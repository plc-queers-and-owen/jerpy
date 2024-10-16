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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'convertToJott'");
    }

    @Override
    public boolean validateTree() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'validateTree'");
    }

    @Override
    public void execute() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'execute'");
    }
    
}
