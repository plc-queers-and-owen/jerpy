package internal.eval;

/**
 * Jott types, including Void
 */
public enum Type {
    Double,
    Integer,
    String,
    Boolean,
    Void;

    /**
     * Returns whether the given string is a type
     */
    public static boolean isType(String string) {
        return switch (string) {
            case "Double" -> true;
            case "Integer" -> true;
            case "String" -> true;
            case "Boolean" -> true;
            case "Void" -> true;
            default -> false;
        };
    }

    /**
     * Returns whether the given string is a valid type for a variable
     */
    public static boolean isVarType(String string) {
        return switch (string) {
            case "Double" -> true;
            case "Integer" -> true;
            case "String" -> true;
            case "Boolean" -> true;
            default -> false;
        };
    }

    public static Type fromString(String string) {
        return switch (string) {
            case "Double" -> Type.Double;
            case "Integer" -> Type.Integer;
            case "String" -> Type.String;
            case "Boolean" -> Type.Boolean;
            case "Void" -> Type.Void;
            default -> null;
        };
    }

    public String toString() {
        switch (this) {
            case Type.Double:
                return "Double";
            case Type.Integer:
                return "Integer";
            case Type.String:
                return "String";
            case Type.Boolean:
                return "Boolean";
            case Type.Void:
                return "Void";
            default:
                return "";

        }
    }
}
