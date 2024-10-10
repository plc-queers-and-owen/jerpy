package internal;

import provided.TokenType;

/**
 * A parsing error.
 * Thrown when the parser encounters an unexpected token.
 */
public class ParseUnexpectedTokenException extends ParseHaltException {
    /**
     * Used internally to generate the error message for a parsing error
     * @param expected what tokens where expected (ex: "\"Def\"", "&lt;eof&gt;)
     * @param found what token was found (ex: "\"Def\"", "&lt;eof&gt;)
     * @return the parsing error message
     */
    private static String generateMessage(String[] expected, String found) {
        StringBuilder builder = new StringBuilder();
        builder.append("expected ");
        for (int i = 0; i < expected.length; i++) {
            if (i == 0) {
                builder.append(expected[i]);
            } else if (i == expected.length - 1) {
                if (i != 1) {
                    builder.append(',');
                }
                builder.append(" or ");
                builder.append(expected[i]);
            } else {
                builder.append(", ");
                builder.append(expected[i]);
            }
        }
        builder.append(", found ");
        builder.append(found);
        return builder.toString();
    }

    /**
     * Used by peekExpect to produce ParseError messages.
     * Has to quote String objects with '"'
     * and quote TokenType objects with '<' and '>'.
     * null is interpreted as "&lt;eof&gt;".
     * @param tokens array of String and TokenType objects
     * @return tokens as a String array which can be output to stderr
     */
    private static String[] peekErrorConvert(Object... tokens) {
        String[] ret = new String[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            ret[i] = switch (tokens[i]) {
                case null -> "<eof>";
                case String s -> "\"" + s + "\"";
                case TokenType t -> "<" + t + ">";
                default -> null;
            };
        }
        return ret;
    }

    /**
     * Create a parsing error, used to terminate parsing on error
     * @param expected what tokens where expected
     * @param found what token was found (ex: "\"Def\"", "&lt;eof&gt;)
     */
    public ParseUnexpectedTokenException(Object[] expected, String found) {
        super(generateMessage(peekErrorConvert(expected), found));
    }
}
