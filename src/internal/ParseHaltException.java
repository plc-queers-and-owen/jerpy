package internal;

/**
 * An error which halts parsing.
 * Caught in JottParser::parse.
 */
public class ParseHaltException extends Exception {
    public ParseHaltException(String msg) {
        super(msg);
    }
}
