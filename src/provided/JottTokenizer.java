package provided;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * This class is responsible for tokenizing Jott code.
 * 
 * @author Dax, Madeline
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
        // The last character added to the token
        char recent = value.charAt(value.length() - 1);
        // The current token string without the last character
        String head = value.substring(0, value.length() - 1);

        switch (context) {
            // Simplify COLON_FC_HEADER into either FC_HEADER or COLON
            case TokenContext.COLON_FC_HEADER -> {
                if (value.equals("::")) {
                    return TokenValidity.createComplete(TokenType.FC_HEADER, "::");
                } else {
                    return TokenValidity.createReject(TokenType.COLON, ":");
                }
            }
            // Simplify REL_OP_ASSIGN into either REL_OP or ASSIGN
            case TokenContext.REL_OP_ASSIGN -> {
                if (value.equals("==")) {
                    return TokenValidity.createComplete(TokenType.REL_OP, "==");
                } else {
                    return TokenValidity.createReject(TokenType.ASSIGN, "=");
                }
            }
            // Completes if we have >= or <= and otherwise just finishes < or > and rejects
            case TokenContext.REL_OP -> {
                if (recent == '=') {
                    return TokenValidity.createComplete(TokenType.REL_OP, value);
                } else {
                    return TokenValidity.createReject(TokenType.REL_OP, head);
                }
            }
            // Checks that a number only has one '.' and only accepts numbers
            case TokenContext.NUMBER -> {
                if (Character.isDigit(recent)) {
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
            }
            // We complete a string if the last character was '"'
            // otherwise we need to keep adding characters to the string literal
            case TokenContext.STRING -> {
                if (recent == '"') {
                    return TokenValidity.createComplete(TokenType.STRING, value);
                } else {
                    return TokenValidity.createContinue(TokenType.STRING, value);
                }
            }
            // Complete NOTEQUAL only if the last character was '='
            case TokenContext.NOTEQUAL -> {
                if (recent == '=') {
                    return TokenValidity.createComplete(TokenType.REL_OP, "!=");
                } else {
                    return TokenValidity.createError(
                            String.format("Unexpected token \"%s\": Expected \"=\".", recent));
                }
            }
            // Accept ID_KEYWORD only if the last character was a letter or number
            case TokenContext.ID_KEYWORD -> {
                if (Character.isAlphabetic(recent) || Character.isDigit(recent)) {
                    return TokenValidity.createAccept(TokenType.ID_KEYWORD, value);
                } else {
                    return TokenValidity.createReject(TokenType.ID_KEYWORD, head);
                }
            }
            // We've encountered an invalid token context, error out
            default -> {
                return TokenValidity.createError("Critical: Invalid token context, missing case handler.");
            }
        }
    }

    /**
     * Tokenizes a single line
     * 
     * @param line   String to tokenize, a single line of the input program
     * @param lineNo Line number
     * @return ArrayList of tokens, or null if errored.
     */
    public ArrayList<Token> tokenize_line(String line, int lineNo) {
        // Create a buffer for the tokens for this line
        ArrayList<Token> tokens = new ArrayList<>();

        // buffer to store multi-character tokens
        TokenContext currentToken = null;
        String currentTokenString = null;

        int characterNumber = 0;

        // loop over every character in the line
        line_loop: while (characterNumber < line.length()) {
            char current = line.charAt(characterNumber);

            if (currentToken == null) {
                // if we haven't started over a token we just check the character
                // to determine what character to start
                 switch(current) {
                     // If we detect a  comment we just stop parsing the line
                     case '#' -> {
                         break line_loop;
                     }
                     case '[' -> tokens.add(this.makeToken(TokenType.L_BRACKET, "[", lineNo));
                    case ']'-> tokens.add(this.makeToken(TokenType.R_BRACKET, "]", lineNo));
                    case '{' -> tokens.add(this.makeToken(TokenType.L_BRACE, "{", lineNo));
                     case '}' -> tokens.add(this.makeToken(TokenType.R_BRACE, "}", lineNo));
                     case ';' -> tokens.add(this.makeToken(TokenType.SEMICOLON, ";", lineNo));
                     case ',' -> tokens.add(this.makeToken(TokenType.COMMA, ",", lineNo));
                     case '+', '*',  '/', '-' ->
                         tokens.add(this.makeToken(TokenType.MATH_OP, String.valueOf(current), lineNo));
                     case '!' -> {
                         currentToken = TokenContext.NOTEQUAL;
                         currentTokenString = "!";
                     }
                     case '=' -> {
                         currentToken = TokenContext.REL_OP_ASSIGN;
                         currentTokenString = "=";
                     }
                     case ':' -> {
                         currentToken = TokenContext.COLON_FC_HEADER;
                         currentTokenString = ":";
                     }
                     case '"' -> {
                         currentToken = TokenContext.STRING;
                         currentTokenString = "\"";
                     }
                     case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.' -> {
                         currentToken = TokenContext.NUMBER;
                         currentTokenString = String.valueOf(current);
                     }
                     case '<', '>' -> {
                         currentToken = TokenContext.REL_OP;
                         currentTokenString = String.valueOf(current);
                     }
                 }

                 // If the character is a letter we have a ID_KEYWORD
                 if(Character.isAlphabetic(current)) {
                    currentToken = TokenContext.ID_KEYWORD;
                    currentTokenString = String.valueOf(current);
                 }

                characterNumber++;
            } else {
                // If we have started a token we add the current character to it
                // and then check if it is still a valid token
                TokenValidity state = this.validate(currentToken, currentTokenString.concat(String.valueOf(current)));
                if (state.isComplete()) {
                    // if we have a valid token we finish that token
                    tokens.add(state.makeToken(this.filename, lineNo));
                    currentToken = null;
                    currentTokenString = null;
                    characterNumber++;
                } else if (state.isReject()) {
                    // if we have a rejecting token, we finish the current token
                    // but don't increment the character number since we'll need to
                    // check the character again as the start of a new token
                    tokens.add(state.makeToken(this.filename, lineNo));
                    currentToken = null;
                    currentTokenString = null;
                } else if (state.isError()) {
                    // if we get an error we abort parsing the current line
                    System.err.println("ERROR: ".concat(state.getError()));
                    return null;
                } else {
                    // the character is a valid addition to the token but
                    // the token can still continue, so we just add the
                    // character to the token and continue tokenizing
                    currentTokenString = currentTokenString.concat(String.valueOf(current));
                    characterNumber++;
                }
            }
        }

        // If we still have a partially complete token we check if it is in a valid state
        // and either finish the token or error
        if (currentToken != null && currentTokenString != null && !currentTokenString.isEmpty()) {
            TokenValidity state = this.validate(currentToken, currentTokenString);
            if (state.isAccepting() || state.isComplete() || state.isReject()) {
                tokens.add(state.makeToken(this.filename, lineNo));
            } else {
                System.err.printf("ERROR: Incomplete/unexpected token \"%s\"%n", currentTokenString);
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
            // Read in the file and create buffer for the output
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            ArrayList<Token> tokens = new ArrayList<>();

            JottTokenizer tokenizer = new JottTokenizer(filename);

            // Read in lines until we reach EOF
            int lineNo = 1;
            try {
                for(String line : reader.lines().toList()) {
                    ArrayList<Token> result = tokenizer.tokenize_line(line, lineNo);

                    // If we've encountered an error we just immediately return null
                    if (result == null) {
                        return null;
                    }

                    tokens.addAll(result);
                    lineNo++;
                }
            } catch (Exception e) {
                System.err.println(e);
            }

            // Close the reader
            reader.close();

            // Print out the tokens
            System.out.println(tokens);

            return tokens;
        } catch (Exception e) {
            // If we've encountered an Exception, print it to stderr and return null
            System.err.println(e);
            return null;
        }
    }
}