package internal.nodes;

import java.util.ArrayList;
import java.util.List;

import internal.ParseHaltException;
import internal.ParseUnexpectedTokenException;
import internal.PeekingArrayIterator;
import internal.SemanticException;
import internal.SemanticTypeException;
import internal.SemanticUsageException;
import internal.scope.Scope;

public class ParamsNode extends Node{
    private final ArrayList<ExprNode> params;

    protected ParamsNode(String filename, int lineNumber, ArrayList<ExprNode> params) {
        super(filename, lineNumber);
        this.params = params;
        this.adopt();
    }

    public static ParamsNode parse(PeekingArrayIterator it) throws ParseUnexpectedTokenException, ParseHaltException {
        ArrayList<ExprNode> params = new ArrayList<>();
        while (true) {
            if(it.peekExpectSafe("]") == 0) {
                return new ParamsNode(it.getCurrentFilename(), it.getCurrentLine(), params);
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
    public boolean validateTree(Scope scope) {
        return this.params.stream().allMatch(p -> p.validateTree(scope));
    }

    /**
     * Checks if all the parameter types match
     * 
     * @param scope      Scope object
     * @param definition Referenced definition
     * @return True if good, false if no
     */
    public boolean validateParameters(Scope scope, FunctionDefNode definition) {
        if (this.params.size() != definition.params.size()) {
            new SemanticUsageException("::" + definition.name + "(...) requires exactly " + definition.params.size()
                    + " parameters, but got " + this.params.size()).report(this);
            return false;
        }
        for (int p = 0; p < this.params.size(); p++) {
            try {
                if (this.params.get(p).inferType(scope) != definition.params.get(0).getType().getType()) {
                    new SemanticTypeException(this.params.get(p).inferType(scope),
                            definition.params.get(0).getType().getType()).report(this.params.get(p));
                    return false;
                }
            } catch (SemanticException e) {
                e.report(this.params.get(p));
                return false;
            }
        }
        return true;
    }

    @Override
    public void execute() {
    }

    @Override
    public List<Node> getChildren() {
        List<Node> children = new ArrayList<>();
        children.addAll(this.params);
        return children;
    }
    
}
