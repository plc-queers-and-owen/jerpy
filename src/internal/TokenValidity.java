package internal;

import provided.Token;
import provided.TokenType;

/*public enum TokenValidity {
    COMPLETE,
    CONTINUE,
    ACCEPT,
    REJECT,
    ERROR
}*/

public class TokenValidity {
    private boolean valid;
    private boolean recoverable;
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

    public boolean isComplete() {
        return this.valid && !this.continuable && !this.recoverable;
    }

    public boolean isContinue() {
        return !this.valid && this.continuable && !this.recoverable;
    }

    public boolean isAccepting() {
        return this.valid && this.continuable && !this.recoverable;
    }

    public boolean isReject() {
        return !this.valid && this.recoverable && !this.continuable;
    }

    public boolean isError() {
        return !this.valid && !this.recoverable && !this.continuable;
    }

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

    public String getError() {
        return this.error;
    }

    public String getContent() {
        return this.content;
    }

    public Token makeToken(String filename, int lineNumber) {
        if (this.isComplete() || this.isAccepting() || this.isReject()) {
            return new Token(this.content, filename, lineNumber, this.type);
        } else {
            return null;
        }
    }

}
