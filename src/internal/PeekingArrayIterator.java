package internal;

import provided.Token;
import provided.TokenType;

import java.util.ArrayList;

/**
 * Ad-hoc iterator that supports peeking
 * Takes elements from an array list
 * Assumes that the underlying array list won't be mutated
 */
public class PeekingArrayIterator {
    private final ArrayList<Token> internal;
    private int idx;

    public PeekingArrayIterator(ArrayList<Token> internal) {
        this.internal = internal;
        this.idx = 0;
    }

    public Token peek() {
        return idx < internal.size() ? internal.get(idx) : null;
    }

    public void skip() {
        if (idx < internal.size()) {
            idx++;
        }
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
     * Peek the next token, expecting an ID_KEYWORD with a given String value
     * or a token of the given TokenType type.
     *
     * @param tokens list of String and TokenType
     * @return index into tokens which matched
     * @throws ParseError on parsing error
     */
    public int peekExpect(Object... tokens) throws ParseError {
        if (idx < internal.size()) {
            Token current = internal.get(idx);
            for (int i = 0; i < tokens.length; i++) {
                if (tokens[i] instanceof String s) {
                    if (current.getToken().equals(s)) {
                        return i;
                    }
                } else if (tokens[i] instanceof TokenType t) {
                    if (current.getTokenType() == t) {
                        return i;
                    }
                }
            }
            throw new ParseError(current.getLineNum(), peekErrorConvert(tokens), current.getToken());
        } else {
            for (int i = 0; i < tokens.length; i++) {
                if (tokens[i] == null) {
                    return i;
                }
            }
            throw new ParseError(getCurrentLine(), peekErrorConvert(tokens), null);
        }
    }

    public int getCurrentLine() {
        if (idx < internal.size()) {
            return internal.get(idx).getLineNum();
        } else if (!internal.isEmpty()) {
            return internal.getLast().getLineNum();
        } else {
            return 1;
        }
    }

    public Token expect(Object... tokens) throws ParseError {
        peekExpect(tokens);
        Token tk =  peek();
        skip();
        return tk;
    }

    public boolean isDone() {
        return idx >= internal.size();
    }
}