package provided;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * This class is responsible for tokenizing Jott code.
 * 
 * @author 
 **/

import java.util.ArrayList;

import internal.TokenContext;
import internal.TokenValidity;

public class JottTokenizer {
    private final String filename;

    /**
     * Constructs a Tokenizer instance, mostly useful for internal state
     * 
     * @param filename
     */
    private JottTokenizer(String filename) {
        this.filename = filename;
    }

    /**
     * Utility function to make a token with internal filename
     * 
     * @param token   Token type
     * @param value   Token value
     * @param lineNum Line number
     * @return
     */
    public Token makeToken(TokenType token, String value, int lineNum) {
        return new Token(value, this.filename, lineNum, token);
    }

    /**
     * Validates an in-progress token
     * 
     * @param context TokenContext
     * @param value   Current value
     * @return TokenValidity instance
     */
    public TokenValidity validate(TokenContext context, String value) {
        char recent = value.charAt(value.length() - 1);
        String head = value.substring(0, value.length() - 1);
        switch (context) {
            case TokenContext.COLON_FC_HEADER:
                if (value.equals("::")) {
                    return TokenValidity.createComplete(TokenType.FC_HEADER, "::");
                } else {
                    return TokenValidity.createReject(TokenType.COLON, ":");
                }
            case TokenContext.REL_OP_ASSIGN:
                if (value.equals("==")) {
                    return TokenValidity.createComplete(TokenType.REL_OP, "==");
                } else {
                    return TokenValidity.createReject(TokenType.ASSIGN, "=");
                }
            case TokenContext.REL_OP:
                if (recent == '=') {
                    return TokenValidity.createComplete(TokenType.REL_OP, value);
                } else {
                    return TokenValidity.createReject(TokenType.REL_OP, head);
                }
            case TokenContext.NUMBER:
                if ("0123456789".indexOf(recent) > -1) {
                    return TokenValidity.createAccept(TokenType.NUMBER, value);
                } else if (recent == '.') {
                    if (value.equals(".")) {
                        return TokenValidity.createError("Unexpected token \".\"");
                    }
                    if (head.contains(".")) {
                        return TokenValidity
                                .createError(String.format(
                                        "Unexpected token \".\": 0-1 decimal points allowed per number token; Got \"%s\"",
                                        value));
                    } else {
                        return TokenValidity.createAccept(TokenType.NUMBER, value);
                    }
                } else {
                    return TokenValidity.createReject(TokenType.NUMBER, head);
                }
            case TokenContext.STRING:
                if (recent == '"') {
                    return TokenValidity.createComplete(TokenType.STRING, value);
                } else {
                    return TokenValidity.createContinue(TokenType.STRING, value);
                }
            case TokenContext.NOTEQUAL:
                if (recent == '=') {
                    return TokenValidity.createComplete(TokenType.REL_OP, "!=");
                } else {
                    return TokenValidity.createError(
                            String.format("Unexpected token \"%s\": Expected \"=\".", String.valueOf(recent)));
                }
            case TokenContext.ID_KEYWORD:
                if ("qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM0123456789".indexOf(recent) > -1) {
                    return TokenValidity.createAccept(TokenType.ID_KEYWORD, value);
                } else {
                    return TokenValidity.createReject(TokenType.ID_KEYWORD, head);
                }
            default:
                return TokenValidity.createError("Critical: Invalid token context, missing case handler.");
        }
    }

    /**
     * Tokenizes a single line
     * 
     * @param line   String to tokenize
     * @param lineNo Line number
     * @return ArrayList of tokens, or null if errored.
     */
    public ArrayList<Token> tokenize_line(String line, int lineNo) {
        ArrayList<Token> tokens = new ArrayList<>();
        TokenContext context = null;
        String stub = null;
        int character = 0;
        while (character < line.length()) {
            char current = line.charAt(character);
            if (context == null) {
                if (current == '#') {
                    break;
                } else if (current == '[') {
                    tokens.add(this.makeToken(TokenType.L_BRACKET, "[", lineNo));
                    character++;
                } else if (current == ']') {
                    tokens.add(this.makeToken(TokenType.R_BRACKET, "]", lineNo));
                    character++;
                } else if (current == '{') {
                    tokens.add(this.makeToken(TokenType.L_BRACE, "{", lineNo));
                    character++;
                } else if (current == '}') {
                    tokens.add(this.makeToken(TokenType.R_BRACE, "}", lineNo));
                    character++;
                } else if (current == ';') {
                    tokens.add(this.makeToken(TokenType.SEMICOLON, ";", lineNo));
                    character++;
                } else if (current == ',') {
                    tokens.add(this.makeToken(TokenType.COMMA, ",", lineNo));
                    character++;
                } else if ("+-/*".indexOf(current) > -1) {
                    tokens.add(this.makeToken(TokenType.MATH_OP, String.valueOf(current), lineNo));
                    character++;
                } else if (current == '!') {
                    context = TokenContext.NOTEQUAL;
                    stub = "!";
                    character++;
                } else if (current == '=') {
                    context = TokenContext.REL_OP_ASSIGN;
                    stub = "=";
                    character++;
                } else if (current == ':') {
                    context = TokenContext.COLON_FC_HEADER;
                    stub = ":";
                    character++;
                } else if (current == '"') {
                    context = TokenContext.STRING;
                    stub = "\"";
                    character++;
                } else if ("0123456789.".indexOf(current) > -1) {
                    context = TokenContext.NUMBER;
                    stub = String.valueOf(current);
                    character++;
                } else if ("<>".indexOf(current) > -1) {
                    context = TokenContext.REL_OP;
                    stub = String.valueOf(current);
                    character++;
                } else if ("qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM".indexOf(current) > -1) {
                    context = TokenContext.ID_KEYWORD;
                    stub = String.valueOf(current);
                    character++;
                } else {
                    character++;
                }
            } else {
                TokenValidity state = this.validate(context, stub.concat(String.valueOf(current)));
                /*
                 * System.out.println(String.
                 * format("Content: %s | COM: %s, REJ: %s, ERR: %s, ACC: %s, CON: %s",
                 * state.getContent(), state.isComplete(),
                 * state.isReject(), state.isError(), state.isAccepting(), state.isContinue()));
                 */
                if (state.isComplete()) {
                    tokens.add(state.makeToken(this.filename, lineNo));
                    context = null;
                    stub = null;
                    character++;
                } else if (state.isReject()) {
                    tokens.add(state.makeToken(this.filename, lineNo));
                    context = null;
                    stub = null;
                } else if (state.isError()) {
                    System.err.println("ERROR: ".concat(state.getError()));
                    return null;
                } else {
                    stub = stub.concat(String.valueOf(current));
                    character++;
                }
            }
        }

        if (context != null && stub != null && stub.length() > 0) {
            TokenValidity state = this.validate(context, stub);
            if (state.isAccepting() || state.isComplete() || state.isReject()) {
                tokens.add(state.makeToken(this.filename, lineNo));
            } else {
                System.err.println(String.format("ERROR: Incomplete/unexpected token \"%s\"", stub));
                return null;
            }
        }

        return tokens;
    }

    /**
     * Takes in a filename and tokenizes that file into Tokens
     * based on the rules of the Jott Language
     * 
     * @param filename the name of the file to tokenize; can be relative or absolute
     *                 path
     * @return an ArrayList of Jott Tokens
     */
    public static ArrayList<Token> tokenize(String filename) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            ArrayList<Token> tokens = new ArrayList<Token>();
            JottTokenizer tokenizer = new JottTokenizer(filename);
            int lineNo = 1;
            while (true) {
                try {
                    String line = reader.readLine();
                    if (line == null) {
                        break;
                    }
                    ArrayList<Token> result = tokenizer.tokenize_line(line, lineNo);
                    if (result == null) {
                        return null;
                    }
                    tokens.addAll(result);
                } catch (Exception e) {
                    break;
                }
                lineNo++;
            }
            reader.close();
            System.out.println(tokens.toString());

            return tokens;
        } catch (Exception e) {
            return null;
        }
    }
}