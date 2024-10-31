package internal.scope;

import java.util.HashMap;

import internal.SemanticException;
import internal.SemanticRedefinitionException;
import internal.SemanticTypeException;
import internal.SemanticUninitializedVariableException;
import internal.SemanticUnknownSymbolException;
import internal.eval.Type;
import internal.eval.TypedValue;

/**
 * A class representing a function's local scope
 */
public class LocalScope {
    private final FunctionSymbol context;
    private boolean completed;
    private HashMap<String, ParameterSymbol> parameters;
    private HashMap<String, VariableSymbol> variables;

    /**
     * Constructor
     * 
     * @param context The parent FunctionSymbol
     */
    public LocalScope(FunctionSymbol context) {
        this.context = context;
        this.parameters = new HashMap<>();
        this.variables = new HashMap<>();
        this.completed = false;
    }

    /**
     * Gets the current context
     * 
     * @return FunctionSymbol
     */
    public FunctionSymbol getContext() {
        return context;
    }

    public boolean isComplete() {
        return this.completed;
    }

    public void finish() {
        this.completed = true;
    }

    /**
     * Gets a mapping of name:parameter
     * 
     * @return Mapping of function params
     */
    public HashMap<String, ParameterSymbol> getParameters() {
        return parameters;
    }

    /**
     * Gets a mapping of name:variable
     * 
     * @return Mapping of local variables
     */
    public HashMap<String, VariableSymbol> getVariables() {
        return variables;
    }

    /**
     * Gets a parameter by name
     * 
     * @param key Parameter symbol name
     * @return ParameterSymbol, if found
     * @throws SemanticUnknownSymbolException Thrown if the param is undefined.
     */
    public ParameterSymbol getParameter(String key) throws SemanticUnknownSymbolException {
        if (this.parameters.containsKey(key)) {
            return this.parameters.get(key);
        } else {
            throw new SemanticUnknownSymbolException(key);
        }
    }

    /**
     * Gets a variable by name
     * 
     * @param key Variable symbol name
     * @return VariableSymbol, if found
     * @throws SemanticUnknownSymbolException Thrown if the variable is undefined.
     */
    public VariableSymbol getVariable(String key) throws SemanticUnknownSymbolException {
        if (this.variables.containsKey(key)) {
            return this.variables.get(key);
        } else {
            throw new SemanticUnknownSymbolException(key);
        }
    }

    /**
     * Checks if a variable or parameter is declared.
     * 
     * @param key Key to check
     * @return True if declared, otherwise false.
     */
    public boolean isDeclared(String key) {
        return this.variables.containsKey(key) || this.parameters.containsKey(key);
    }

    /**
     * Checks if a variable or parameter is both declared *and* has a value
     * 
     * @param key Key to check
     * @return True if the symbol has a value and is declared, otherwise false.
     */
    public boolean isInitialized(String key) {
        if (this.parameters.containsKey(key)) {
            return this.parameters.get(key).hasValue();
        } else if (this.variables.containsKey(key)) {
            return this.variables.get(key).hasValue();
        } else {
            return false;
        }
    }

    /**
     * Gets the value of a specific symbol
     * 
     * @param key Symbol to get
     * @return Symbol value
     * @throws SemanticException Thrown if the symbol is undefined or has no value
     */
    public TypedValue getValue(String key) throws SemanticException {
        if (this.parameters.containsKey(key)) {
            TypedValue value = this.parameters.get(key).getValue();
            if (value == null) {
                throw new SemanticUninitializedVariableException(key);
            }
            return value;
        } else if (this.variables.containsKey(key)) {
            TypedValue value = this.variables.get(key).getValue();
            if (value == null) {
                throw new SemanticUninitializedVariableException(key);
            }
            return value;
        } else {
            throw new SemanticUnknownSymbolException(key);
        }
    }

    /**
     * Gets the type of a specific symbol
     * 
     * @param key Symbol to get
     * @return Symbol type
     * @throws SemanticException Thrown if the symbol is undefined
     */
    public Type getType(String key) throws SemanticException {
        if (this.parameters.containsKey(key)) {
            return this.parameters.get(key).getType();
        } else if (this.variables.containsKey(key)) {
            return this.variables.get(key).getType();
        } else {
            throw new SemanticUnknownSymbolException(key);
        }
    }

    /**
     * Defines a parameter
     * 
     * @param symbol Symbol to define
     * @throws SemanticException Thrown if the symbol is already declared
     */
    public void define(ParameterSymbol symbol) throws SemanticException {
        if (this.isDeclared(symbol.getSymbol())) {
            throw new SemanticRedefinitionException(symbol.getSymbol());
        }

        this.parameters.put(symbol.getSymbol(), symbol);
    }

    /**
     * Defines a variable
     * 
     * @param symbol Symbol to define
     * @throws SemanticException Thrown if the symbol is already declared
     */
    public void define(VariableSymbol symbol) throws SemanticException {
        if (this.isDeclared(symbol.getSymbol())) {
            throw new SemanticRedefinitionException(symbol.getSymbol());
        }

        this.variables.put(symbol.getSymbol(), symbol);
    }

    /**
     * Sets the value of a param/var
     * 
     * @param key   Symbol to set value
     * @param value Value to set
     * @throws SemanticException Thrown if the symbol doesn't exist, or if the types
     *                           mismatch.
     */
    public void setValue(String key, TypedValue value) throws SemanticException {
        if (this.parameters.containsKey(key)) {
            ParameterSymbol symbol = this.parameters.get(key);
            if (symbol.getType() != value.getType()) {
                throw new SemanticTypeException(value.getType(), symbol.getType());
            }
            symbol.setValue(value);
        } else if (this.variables.containsKey(key)) {
            VariableSymbol symbol = this.variables.get(key);
            if (symbol.getType() != value.getType()) {
                throw new SemanticTypeException(value.getType(), symbol.getType());
            }
            symbol.setValue(value);
        } else {
            throw new SemanticUnknownSymbolException(key);
        }
    }

}
