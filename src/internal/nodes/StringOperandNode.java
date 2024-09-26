package internal.nodes;

public class StringOperandNode extends OperandNode {
    private final String val;

    protected StringOperandNode(int lineNumber, String val) {
        super(lineNumber);
        this.val = val;
    }

    @Override
    public String convertToJott() {
        return "\"" + val + "\""; // no need to escape anything
    }

    @Override
    public boolean validateTree() {
        return true;
    }

    @Override
    public void execute() {
    }
}
