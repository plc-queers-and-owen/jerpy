package internal.nodes;
import java.util.List;
import provided.JottTree;
import java.util.ArrayList;

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
     * Gets the closest parent of type T (Class instance of Node subclass)
     * 
     * @param <T>  Node type (implicit)
     * @param node Node class instance: ie ParamsNode.class
     * @return A node of type T if found, or null if not
     */
    public <T extends Node> T getClosestParent(Class<T> node) {
        if (this.getParent() == null) {
            return null;
        }
        if (node.isInstance(this.getParent())) {
            return node.cast(this.getParent());
        } else {
            return this.getParent().getClosestParent(node);
        }
    }

    /**
     * Get all children matching a specific Node subclass
     * 
     * @param <T>  Node type (implicit)
     * @param node Node class instance: ie ParamsNode.class
     * @return A List<T> of nodes
     */
    public <T extends Node> List<T> getChildrenLike(Class<T> node) {
        ArrayList<T> results = new ArrayList<>();
        for (Node item : this.getChildren()) {
            if (node.isInstance(item)) {
                results.add(node.cast(item));
            }

            results.addAll(item.getChildrenLike(node));
        }

        return results;
    }

    public String getSymbol() {
        return null;
    }
}
