package internal.nodes;

import java.util.Arrays;
import java.util.List;

import provided.JottTree;

/**
 * Abstract base class for AST nodes
 * Stores a line number and requires child classes to implement JottTree
 */
public abstract class Node implements JottTree {
    private final int lineNumber;
    private Node parent;

    protected Node(int lineNumber) {
        this.lineNumber = lineNumber;
        this.parent = null;
    }

    /**
     * Sets the current node as the parent of all its children
     * (Must be called separately because variables won't be assigned yet in the
     * constructor)
     */
    protected void adopt() {
        for (Node child : this.getChildren()) {
            child.setParent(this);
        }
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public Node getParent() {
        return this.parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    /**
     * Gets a List<Node> of all of this node's children.
     * This is mostly only useful for traversal,
     * the order in which each child will appear is not necessarily defined.
     * 
     * @return List of Node objects
     */
    public abstract List<Node> getChildren();

    /**
     * Gets the closest node (upward) matching the specified name(s), possibly the
     * current node.
     * 
     * @param names Node class name(s) (ie TypeNode). This does not account for
     *              subclasses.
     * @return First match against any of the specified names, or null if no match
     */
    public Node getClosest(String... names) {
        if (Arrays.asList(names).contains(this.getClass().getSimpleName())) {
            return this;
        } else if (this.parent == null) {
            return null;
        } else {
            return this.parent.getClosest(names);
        }
    }

    /**
     * Gets the closest node (upward) matching the specified name(s), not including
     * the current node
     * 
     * @param names Node class name(s) (ie TypeNode). This does not account for
     *              subclasses.
     * @return First match against any of the specified names, or null if no match
     */
    public Node getClosestParent(String... names) {
        if (this.parent == null) {
            return null;
        } else {
            return this.parent.getClosest(names);
        }
    }
}
