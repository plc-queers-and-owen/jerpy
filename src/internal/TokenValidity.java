package internal;

import provided.Token;
import provided.TokenType;

/**
 * Utility class for storing token validity data
 * 
 * @param filename   Token filename
 * @param lineNumber Token line number
 */
public class TokenValidity {
    private boolean valid; // Whether the token is CURRENTLY valid
    private boolean recoverable; // Whether the token is CURRENTLY invalid but its previous state was not
    private boolean continuable; // Whether the token can be further extended from this point
    private TokenType type;
    private String content;
    private String error;

    private TokenValidity(TokenType type, String content, boolean valid, boolean recoverable, boolean continuable,
            String error) {
        this.valid = valid;
        this.recoverable = recoverable;
        this.continuable = continuable;

        this.type = type;
        this.content = content;
        this.error = error;
    }

    public boolean isComplete() { // The token is valid & cannot be continued from this point
        return this.valid && !this.continuable && !this.recoverable;
    }

    public boolean isContinue() { // The token is currently invalid but may be valid if continued
        return !this.valid && this.continuable && !this.recoverable;
    }

    public boolean isAccepting() { // The token is currently valid & may also be continued
        return this.valid && this.continuable && !this.recoverable;
    }

    public boolean isReject() { // The token is invalid, but the previous state was valid
        return !this.valid && this.recoverable && !this.continuable;
    }

    public boolean isError() { // The token is invalid, and unrecoverably incomplete
        return !this.valid && !this.recoverable && !this.continuable;
    }

    // Helper functions to create each type of TokenValidity
    public static TokenValidity createComplete(TokenType type, String content) {
        return new TokenValidity(type, content, true, false, false, null);
    }

    public static TokenValidity createContinue(TokenType type, String content) {
        return new TokenValidity(type, content, false, false, true, null);
    }

    public static TokenValidity createAccept(TokenType type, String content) {
        return new TokenValidity(type, content, true, false, true, null);
    }

    public static TokenValidity createReject(TokenType type, String content) {
        return new TokenValidity(type, content, false, true, false, null);
    }

    public static TokenValidity createError(String error) {
        return new TokenValidity(null, null, false, false, false, error);
    }

    // Get the current error, if any
    public String getError() {
        return this.error;
    }

    // Get the token content, if any
    public String getContent() {
        return this.content;
    }

    // Creates a token from the contained content, if possible
    public Token makeToken(String filename, int lineNumber) {
        if (this.isComplete() || this.isAccepting() || this.isReject()) { // Also accept Reject here, rely on external
                                                                          // checks to confirm/transform value
            return new Token(this.content, filename, lineNumber, this.type);
        } else {
            return null;
        }
    }

}
