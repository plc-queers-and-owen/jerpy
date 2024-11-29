package internal;

import internal.nodes.Node;

public class ExecutionException extends Exception {
    private String reason;

    public String getReason() {
        return reason;
    }

    private Node node;

    public Node getNode() {
        return node;
    }

    public ExecutionException(String reason, Node node) {
        super(String.format("Error: %s\nCaused by: %s on line %d in %s.", reason, node.convertToJott(),
                node.getLineNumber(), node.getFilename()));
        this.reason = reason;
        this.node = node;
    }
}
