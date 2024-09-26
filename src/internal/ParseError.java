package internal;

/**
 * Thrown when the parser encounters an unexpected token
 * Thrown by static parse methods in individual Node classes
 * Caught by JottParser::parse
 */
public class ParseError extends Exception {
    /**
     * Used internally to generate the error message for a parsing error
     * @param line the line the error occurred on
     * @param expected what tokens where expected (ex: "\"Def\"", "&lt;eof&gt;)
     * @param found what token was found (ex: "\"Def\"", "&lt;eof&gt;)
     * @return the parsing error message
     */
    private static String generateMessage(int line, String[] expected, String found) {
        StringBuilder builder = new StringBuilder();
        builder.append("[line ");
        builder.append(line);
        builder.append("] syntax error: expected ");
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
     * Create a parsing error, used to terminate parsing on error
     * @param line the line the error occurred on
     * @param expected what tokens where expected (ex: "\"Def\"", "&lt;eof&gt;)
     * @param found what token was found (ex: "\"Def\"", "&lt;eof&gt;)
     * @return the parsing error message
     */
    public ParseError(int line, String[] expected, String found) {
        super(generateMessage(line, expected, found));
    }
}
