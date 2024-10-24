import java.util.ArrayList;

import provided.JottParser;
import provided.JottTokenizer;
import provided.JottTree;
import provided.Token;

public class Jott {

    public static void main(String[] args) {
        ArrayList<Token> tokens = JottTokenizer.tokenize(args[0]);
        JottTree parsedTree = JottParser.parse(tokens);
        if (parsedTree == null) {
            return;
        } else {
            {
                System.out.println(parsedTree.convertToJott());
            }
        }

    }

}
