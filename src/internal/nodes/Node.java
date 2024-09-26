package internal.nodes;

import provided.JottTree;

/**
 * Abstract base class for AST nodes
 * Stores a line number and requires child classes to implement JottTree
 */
public abstract class Node implements JottTree {
    private final int lineNumber;

    protected Node(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public int getLineNumber() {
        return lineNumber;
    }
}
