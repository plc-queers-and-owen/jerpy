package internal.eval;

import internal.SemanticException;
import internal.SemanticTypeException;

public class TypedValue {
    private final Type type;

    public Type getType() {
        return type;
    }

    private final String value;

    public TypedValue(Type type, String value) {
        this.type = type;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public double getDouble() throws SemanticException {
        if (this.type == Type.Double) {
            try {
                return Double.parseDouble(this.value);
            } catch (NullPointerException | NumberFormatException e) {
                throw new SemanticException("Value stored with inappropriate type.");
            }
        } else {
            throw new SemanticTypeException(this.type, Type.Double);
        }
    }

    public int getInteger() throws SemanticException {
        if (this.type == Type.Integer) {
            try {
                return Integer.parseInt(this.value);
            } catch (NumberFormatException e) {
                throw new SemanticException("Value stored with inappropriate type.");
            }
        } else {
            throw new SemanticTypeException(this.type, Type.Integer);
        }
    }

    public String getString() throws SemanticException {
        if (this.type == Type.String) {
            return this.value;
        } else {
            throw new SemanticTypeException(this.type, Type.String);
        }
    }

    public Boolean getBoolean() throws SemanticException {
        if (this.type == Type.Boolean) {
            switch (this.value) {
                case "True":
                    return true;
                case "False":
                    return false;
                default:
                    throw new SemanticException("Value stored with inappropriate type.");
            }
        } else {
            throw new SemanticTypeException(this.type, Type.Boolean);
        }
    }

    public double getNumericOperand() throws SemanticException {
        switch (this.type) {
            case Type.Double:
                return this.getDouble();
            case Type.Integer:
                return (double) this.getInteger();
            default:
                throw new SemanticException("Value stored with inappropriate type.");
        }
    }

    public TypedValue(double value) {
        this.type = Type.Double;
        this.value = String.valueOf(value);
    }

    public TypedValue(int value) {
        this.type = Type.Integer;
        this.value = String.valueOf(value);
    }

    public TypedValue(String value) {
        this.type = Type.String;
        this.value = value;
    }

    public TypedValue(Boolean value) {
        this.type = Type.Boolean;
        if (value) {
            this.value = "True";
        } else {
            this.value = "False";
        }
    }
}
