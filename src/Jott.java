import java.util.ArrayList;

import provided.JottParser;
import provided.JottTokenizer;
import provided.Token;

public class Jott {

    public static void main(String[] args) {
        ArrayList<Token> tokens = JottTokenizer.tokenize(args[0]);
        JottParser.parse(tokens);
    }
}
