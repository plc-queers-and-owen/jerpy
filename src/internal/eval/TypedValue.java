package internal.eval;

public abstract class TypedValue<T> {
    private final Type type;

    public Type getType() {
        return type;
    }

    private final T value;

    public T getValue() {
        return value;
    }

    public TypedValue(Type type, T value) {
        this.type = type;
        this.value = value;
    }
}
