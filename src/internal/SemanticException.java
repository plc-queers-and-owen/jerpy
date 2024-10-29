package internal;

import internal.nodes.Node;

public class SemanticException extends Exception {
    public SemanticException(String msg) {
        super(msg);
    }

    public void report(Node source) {
        System.err.println("Semantic Error\n" + this.getMessage() + "\n" + source.getFilename() + ":"
                + String.valueOf(source.getLineNumber()));
    }
}
