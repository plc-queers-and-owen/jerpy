package internal.scope;

import internal.SemanticException;
import internal.eval.Type;
import internal.nodes.ParamsNode;

public interface FunctionSymbol {
    /**
     * @return The name of the function defined by this symbol
     */
    public String name();

    /**
     * @return The number of parameters this function takes
     */
    public int paramCount();

    /**
     * Gets the type of a parameter
     * 
     * @param idx The index of the paramter
     * @return The type of the parameter at idx or null if idx is out of bounds
     */
    public Type paramType(int idx);

    /**
     * The return type of the function
     */
    public Type returnType();

    /**
     * Whether the FunctionSymbol is a builtin or not
     * 
     * If this is false it is safe to cast to {@link DefinedFunctionSymbol}
     */
    public boolean isBuiltin();

    /**
     * Checks if all the parameter types match
     * 
     * @param scope  The scope we're validating from
     * @param params A ParamsNode to be used as function call parameters
     * @return True if good, false if no
     */
    public boolean validateParameters(Scope scope, ParamsNode params) throws SemanticException;

    public void execute();
}
