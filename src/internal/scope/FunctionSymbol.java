package internal.scope;

import internal.eval.Type;

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
     * Whether the FunctionSymbol is a builtin or not
     * 
     * If this is false it is safe to cast to {@link DefinedFunctionSymbol}
     */
    public boolean isBuiltin();

    public void execute();
}
