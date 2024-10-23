package internal.scope;

import java.util.HashMap;

import internal.SemanticException;
import internal.SemanticRedefinitionException;
import internal.SemanticUnknownSymbolException;

public class Scope {
    private HashMap<String, VariableSymbol> variables;
    private HashMap<String, FunctionSymbol> functions;

    public Scope() {
        this.variables = new HashMap<>();
        this.functions = new HashMap<>();
    }

    public HashMap<String, VariableSymbol> getVariables() {
        return variables;
    }

    public HashMap<String, FunctionSymbol> getFunctions() {
        return functions;
    }

    public VariableSymbol getVariable(String name) throws SemanticException {
        if (this.variables.containsKey(name)) {
            return this.variables.get(name);
        } else {
            throw new SemanticUnknownSymbolException(name);
        }
    }

    public FunctionSymbol getFunction(String name) throws SemanticException {
        if (this.functions.containsKey(name)) {
            return this.functions.get(name);
        } else {
            throw new SemanticUnknownSymbolException(name);
        }
    }

    public void define(VariableSymbol symbol) throws SemanticException {
        if (this.variables.containsKey(symbol.getSymbol())) {
            throw new SemanticRedefinitionException(symbol.getSymbol());
        } else {
            this.variables.put(symbol.getSymbol(), symbol);
        }
    }

    public void define(FunctionSymbol symbol) throws SemanticException {
        if (this.functions.containsKey(symbol.getSymbol())) {
            throw new SemanticRedefinitionException(symbol.getSymbol());
        } else {
            this.functions.put(symbol.getSymbol(), symbol);
        }
    }
}
