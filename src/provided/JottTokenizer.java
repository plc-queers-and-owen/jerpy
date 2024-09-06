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

public class JottTokenizer {
    private final String filename;

    private JottTokenizer(String filename) {
        this.filename = filename;
    }

    public Token makeToken(TokenType token, String value, int lineNum) {
        return new Token(value, this.filename, lineNum, token);
    }

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
                }
                if (current == '[') {
                    tokens.add(this.makeToken(TokenType.L_BRACKET, "[", lineNo));
                    character++;
                    continue;
                }

                if (current == ']') {
                    tokens.add(this.makeToken(TokenType.R_BRACKET, "]", lineNo));
                    character++;
                    continue;
                }

                if (current == '{') {
                    tokens.add(this.makeToken(TokenType.L_BRACE, "{", lineNo));
                    character++;
                    continue;
                }

                if (current == '}') {
                    tokens.add(this.makeToken(TokenType.R_BRACE, "}", lineNo));
                    character++;
                    continue;
                }

                if (current == ';') {
                    tokens.add(this.makeToken(TokenType.SEMICOLON, ";", lineNo));
                    character++;
                    continue;
                }

                if (current == ',') {
                    tokens.add(this.makeToken(TokenType.COMMA, ",", lineNo));
                    character++;
                    continue;
                }

                if ("+-/*".indexOf(current) > -1) {
                    tokens.add(this.makeToken(TokenType.MATH_OP, String.valueOf(current), lineNo));
                    character++;
                    continue;
                }
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
            int lineNo = 0;
            while (true) {
                try {
                    String line = reader.readLine();
                    if (line == null) {
                        break;
                    }
                    tokens.addAll(tokenizer.tokenize_line(line, lineNo));
                } catch (Exception e) {
                    break;
                }
                lineNo++;
            }
            reader.close();

            return tokens;
        } catch (Exception e) {
            return new ArrayList<Token>();
        }
    }
}