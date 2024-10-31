package internal;

import internal.eval.Type;

public class SemanticTypeException extends SemanticException {
    public SemanticTypeException(Type found, Type expected) {
        super("Type Error: Expected type " + expected.toString() + ", found type " + found.toString() + ".");
    }
}
