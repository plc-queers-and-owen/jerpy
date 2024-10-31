package internal.scope;

import java.util.HashMap;

import internal.SemanticException;
import internal.SemanticRedefinitionException;
import internal.SemanticUnknownSymbolException;

/**
 * A class representing the global scope, and containing the local scope of each
 * function.
 */
public class Scope {
    private HashMap<String, LocalScope> scopes;
    private String currentScope;

    /**
     * Gets a mapping of function name : local scope
     * 
     * @return Mapping of Name:Scope
     */
    public HashMap<String, LocalScope> getScopes() {
        return scopes;
    }

    /**
     * Gets the name of the current scope
     * 
     * @return Current scope name, or null.
     */
    public String getCurrentScopeName() {
        return this.currentScope;
    }

    /**
     * Gets the current scope
     * 
     * @return Current scope, or null.
     */
    public LocalScope getCurrentScope() {
        if (this.currentScope == null) {
            return null;
        }
        return this.scopes.get(currentScope);
    }

    /**
     * Enables the specified scope as the current scope.
     * 
     * @param scope Current scope name
     * @throws SemanticException Throws if the scope name is not yet defined.
     */
    public void enableScope(String scope) throws SemanticException {
        if (this.scopes.containsKey(scope)) {
            this.currentScope = scope;
        } else {
            throw new SemanticUnknownSymbolException(scope);
        }
    }

    /**
     * Sets the current scope to null.
     */
    public void clearScope() {
        this.currentScope = null;
    }

    /**
     * Defines a function symbol and initializes its local scope.
     * 
     * @param func Function symbol
     * @throws SemanticException Thrown if the function has already been defined.
     */
    public void define(FunctionSymbol func) throws SemanticException {
        if (this.scopes.containsKey(func.getSymbol())) {
            throw new SemanticRedefinitionException(func.getSymbol());
        }
        this.scopes.put(func.getSymbol(), new LocalScope(func));
    }

    /**
     * Gets a scope by name
     * 
     * @param scope Scope name
     * @return LocalScope object, if it exists.
     * @throws SemanticException Thrown if the scope name is unknown.
     */
    public LocalScope getScope(String scope) throws SemanticException {
        if (this.isComplete(scope)) {
            return this.scopes.get(scope);
        } else {
            throw new SemanticUnknownSymbolException(scope);
        }
    }

    public Scope() {
        this.scopes = new HashMap<>();
        this.currentScope = null;
    }

    public boolean isComplete(String name) {
        return this.scopes.get(name) != null && this.scopes.get(name).isComplete();
    }

    public void finish(String name) {
        if (this.scopes.containsKey(name)) {
            this.scopes.get(name).finish();
        }
    }
}
