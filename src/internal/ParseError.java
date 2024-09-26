package internal;

public class ParseError extends Exception {
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

    public ParseError(int line, String[] expected, String found) {
        super(generateMessage(line, expected, found));
    }
}
