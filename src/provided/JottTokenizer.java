package provided;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * This class is responsible for tokenizing Jott code.
 * 
 * @author 
 **/

import java.util.ArrayList;

public class JottTokenizer {
    public static ArrayList<Token> tokenize_line(String line) {
        ArrayList<Token> tokens = new ArrayList<>();
        System.out.println(line);

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
            while (true) {
                try {
                    String line = reader.readLine();
                    if (line == null) {
                        break;
                    }
                    tokens.addAll(JottTokenizer.tokenize_line(line));
                } catch (Exception e) {
                    break;
                }
            }
            reader.close();

            return tokens;
        } catch (Exception e) {
            return new ArrayList<Token>();
        }
    }
}