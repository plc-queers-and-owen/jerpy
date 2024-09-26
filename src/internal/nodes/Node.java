package internal.nodes;

import provided.JottTree;

public abstract class Node implements JottTree {
    private final int lineNumber;

    protected Node(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public int getLineNumber() {
        return lineNumber;
    }
}
