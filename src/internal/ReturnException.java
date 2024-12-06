package internal;

import internal.eval.TypedValue;

public class ReturnException extends Exception {
    private final TypedValue val;

    public ReturnException(TypedValue val) {
        super("<return exception>");
        this.val = val;
    }

    public TypedValue getValue() {
        return val;
    }
}
