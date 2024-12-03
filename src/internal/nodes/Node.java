package internal.nodes;
import java.util.List;

import internal.ExecutionException;
import internal.SemanticException;
import internal.eval.TypedValue;
import internal.scope.Scope;
import provided.JottTree;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Abstract base class for AST nodes
 * Stores a line number and requires child classes to implement JottTree
 */
public abstract class Node implements JottTree {
    private final int lineNumber;
    private final String filename;

    public String getFilename() {
        return filename;
    }

    private Node parent;

    protected Node(String filename, int lineNumber) {
        this.filename = filename;
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

    public TypedValue evaluate(Scope scope) throws ExecutionException, SemanticException {
        return null;
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
     * Gets the closest parent that matches any of the supplied node names
     * 
     * @param nodeNames Any number of node names to search for (ie ElseNode,
     *                  ExprNode, etc)
     * @return The resulting Node, or null if none were found.
     */
    public Node getAnyClosestParent(String... nodeNames) {
        if (this.getParent() == null) {
            return null;
        }
        if (Arrays.asList(nodeNames).contains(this.getParent().getClass().getSimpleName())) {
            return this.getParent();
        } else {
            return this.getParent().getAnyClosestParent(nodeNames);
        }
    }

    /**
     * Helper to check if this node is in a loop
     * 
     * @return True if in a loop, false otherwise
     */
    public boolean isLooped() {
        return this.getClosestParent(WhileLoopNode.class) != null;
    }

    /**
     * Helper to get the enclosing BodyNode.
     * 
     * @return The closest enclosure, or null if invalid context
     */
    public BodyNode getEnclosingBody() {
        return this.getClosestParent(BodyNode.class);
    }

    /**
     * Gets the function definition this node is inside.
     * 
     * @return The enclosing FunctionDefNode, or null if invalid context
     */
    public FunctionDefNode getEnclosingFunction() {
        return this.getClosestParent(FunctionDefNode.class);
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

    /**
     * Helper function to check if an id is valid
     * 
     * @param id ID string to check
     * @return True if valid, false otherwise
     */
    public static boolean validateId(String id) {
        if (id.length() == 0) {
            return false;
        } else {
            return Character.isLowerCase(id.charAt(0));
        }
    }
}
