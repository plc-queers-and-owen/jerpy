package internal.scope;
import internal.eval.Type;
import internal.eval.TypedValue;
import internal.nodes.VariableDeclarationNode;

public class VariableSymbol extends SymbolItem<VariableDeclarationNode> {
    private TypedValue value;
    private boolean initialized;
    public VariableSymbol(String symbol, int definedAt, VariableDeclarationNode target) {
        super(symbol, definedAt, target);
        this.value = null;
        this.initialized = false;
    }

    public VariableSymbol(String symbol, int definedAt, VariableDeclarationNode target, TypedValue value) {
        super(symbol, definedAt, target);
        this.value = value;
        this.initialized = true;
    }

    public static VariableSymbol from(VariableDeclarationNode target) {
        return new VariableSymbol(target.getSymbol(), target.getLineNumber(), target);
    }

    public Type getType() {
        return this.target.getType().getType();
    }

    public String getName() {
        return this.target.getName();
    }

    public TypedValue getValue() {
        return this.value;
    }

    public void setValue(TypedValue value) {
        this.value = value;
    }

    public boolean hasValue() {
        return this.value != null;
    }

    @Override
    public SymbolType getSymbolType() {
        return SymbolType.Variable;
    }

    @Override
    public void initialize() {
        this.initialized = true;
    }

    @Override
    public boolean isInitialized() {
        return this.initialized;
    }
}
