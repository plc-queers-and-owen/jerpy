import java.util.ArrayList;

import internal.nodes.ProgramNode;
import provided.JottParser;
import provided.JottTokenizer;
import provided.Token;

public class Jott {

    public static void main(String[] args) {
        ArrayList<Token> tokens = JottTokenizer.tokenize(args[0]);
        ProgramNode program = (ProgramNode) JottParser.parse(tokens);
        if (program != null) {
            System.out.println(program.convertToJott());
        }
    }
}
