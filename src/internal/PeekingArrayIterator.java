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

    /**
     * Construct a PeekingArrayIterator from an ArrayList&lt;Token&gt;
     * @param internal the list of tokens to iterate over
     */
    public PeekingArrayIterator(ArrayList<Token> internal) {
        this.internal = internal;
        this.idx = 0;
    }

    /**
     * Peeks the current token
     * @return the current token, null on &lt;eof&gt;
     */
    public Token peek() {
        return idx < internal.size() ? internal.get(idx) : null;
    }

    /**
     * Skips the current token
     */
    public void skip() {
        if (idx < internal.size()) {
            idx++;
        }
    }

    /**
     * Skips backward 1 token
     */
    public void back() {
        if (idx > 0) {
            idx--;
        }
    }

    public String getContext() {
        return String.join("",
                this.internal
                        .subList(Math.clamp(idx - 10000, 0, this.internal.size()),
                                Math.clamp(idx, 0, this.internal.size()))
                        .stream().map(value -> value.getToken()).toList())
                + "  --> " + this.internal.get(Math.clamp(idx, 0, this.internal.size() - 1)).getToken() + " <--  "
                + String.join("",
                                this.internal
                                .subList(Math.clamp(idx + 1, 0, this.internal.size()),
                                        Math.clamp(idx + 100000, 0, this.internal.size()))
                                .stream().map(value -> value.getToken()).toList());
    }

    /**
     * Peek the next token, expecting an ID_KEYWORD with a given String value
     * or a token of the given TokenType type.
     *
     * @param tokens list of String and TokenType
     * @return index into tokens which matched
     * @throws ParseUnexpectedTokenException on parsing error
     */
    public int peekExpect(Object... tokens) throws ParseUnexpectedTokenException {
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
            throw new ParseUnexpectedTokenException(tokens, current.getToken(), this.getContext());
        } else {
            for (int i = 0; i < tokens.length; i++) {
                if (tokens[i] == null) {
                    return i;
                }
            }
            throw new ParseUnexpectedTokenException(tokens, null, this.getContext());
        }
    }

    /**
     * Does a peekExpect without throwing an exception. If the expectation isn't
     * met, returns -1.
     * 
     * @param tokens list of String and TokenType
     * @return index into tokens which matched, or -1 if no match
     */
    public int peekExpectSafe(Object... tokens) {
        try {
            return peekExpect(tokens);
        } catch (ParseUnexpectedTokenException e) {
            return -1;
        }
    }

    /**
     * Gets the current line
     * @return the line associated with the current token, or on &lt;eof&gt;, the line associated with the last token
     */
    public int getCurrentLine() {
        if (idx < internal.size()) {
            return internal.get(idx).getLineNum();
        } else if (!internal.isEmpty()) {
            return internal.getLast().getLineNum();
        } else {
            return 1;
        }
    }

    /**
     * Gets the current filename
     * @return the line associated with the current token, or on &lt;eof&gt;, the line associated with the last token
     */
    public String getCurrentFilename() {
        if (idx < internal.size()) {
            return internal.get(idx).getFilename();
        } else if (!internal.isEmpty()) {
            return internal.getLast().getFilename();
        } else {
            return "???";
        }
    }

    /**
     * Like peekExpect, but returns the current token instead of the match index
     * and skips to the next token
     * @param tokens list of String and TokenType
     * @return the current matching token
     * @throws ParseUnexpectedTokenException on parsing error
     */
    public Token expect(Object... tokens) throws ParseUnexpectedTokenException {
        peekExpect(tokens);
        Token tk =  peek();
        skip();
        return tk;
    }

    /**
     * Checks if this iterator has hit &lt;eof&gt;
     * @return true iff this iterator is finished
     */
    public boolean isDone() {
        return idx >= internal.size();
    }
}
