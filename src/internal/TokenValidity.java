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
    /** Whether the token is CURRENTLY valid */
    private boolean valid;
    /** Whether the token is CURRENTLY invalid but its previous state was not */
    private boolean recoverable;
    /** Whether the token can be further extended from this point */
    private boolean continuable;
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

    /**
     * Check if the token is valid & cannot be continued from this point
     */
    public boolean isComplete() {
        return this.valid && !this.continuable && !this.recoverable;
    }

    /**
     * Check if the token is currently invalid but may be valid if continued
     */
    public boolean isContinue() {
        return !this.valid && this.continuable && !this.recoverable;
    }

    /**
     * Check if the token is currently valid & may also be continued
     */
    public boolean isAccepting() {
        return this.valid && this.continuable && !this.recoverable;
    }

    /**
     * Check if the token is invalid, but the previous state was valid
     */
    public boolean isReject() {
        return !this.valid && this.recoverable && !this.continuable;
    }

    /**
     * Check if the token is invalid, and unrecoverably incomplete
     */
    public boolean isError() {
        return !this.valid && !this.recoverable && !this.continuable;
    }

    // Helper methods to create each type of TokenValidity

    /**
     * A token that is valid, and cannot be added to
     */
    public static TokenValidity createComplete(TokenType type, String content) {
        return new TokenValidity(type, content, true, false, false, null);
    }

    /**
     * A token that is not yet valid, but can still be added to
     */
    public static TokenValidity createContinue(TokenType type, String content) {
        return new TokenValidity(type, content, false, false, true, null);
    }

    /**
     * A token that is valid, but can still be added to
     */
    public static TokenValidity createAccept(TokenType type, String content) {
        return new TokenValidity(type, content, true, false, true, null);
    }

    /**
     * A token that is not valid, but can be recovered
     */
    public static TokenValidity createReject(TokenType type, String content) {
        return new TokenValidity(type, content, false, true, false, null);
    }

    /**
     * A token that is not valid, and in a state where it cannot be recovered
     */
    public static TokenValidity createError(String error) {
        return new TokenValidity(null, null, false, false, false, error);
    }

    /**
     * Get the current error, if any
     */
    public String getError() {
        return this.error;
    }

    /**
     * Get the token content, if any
     */
    public String getContent() {
        return this.content;
    }

    /**
     * Creates a token from the contained content, if possible
     */
    public Token makeToken(String filename, int lineNumber) {
        // Also accept Reject here, rely on external checks to confirm/transform value
        if (this.isComplete() || this.isAccepting() || this.isReject()) {
            return new Token(this.content, filename, lineNumber, this.type);
        } else {
            return null;
        }
    }

}
