package internal;

/**
 * A parsing error.
 * Thrown when an identifier is expected, but the next token is an
 * ID_KEYWORD which starts with an uppercase letter.
 */
public class ParseLowerCaseIdentifierException extends ParseHaltException {
    public ParseLowerCaseIdentifierException() {
        super("identifier must start with a lowercase character");
    }
}
