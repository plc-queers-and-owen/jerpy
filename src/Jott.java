import java.util.ArrayList;

import internal.nodes.ProgramNode;
import internal.scope.Scope;
import provided.JottParser;
import provided.JottTokenizer;
import provided.Token;

public class Jott {

    public static void main(String[] args) {
        Scope scope = new Scope();

        ArrayList<Token> tokens = JottTokenizer.tokenize(args[0]);
        ProgramNode program = (ProgramNode) JottParser.parse(tokens);
        if (program.validateTree(scope)) {
            // TODO: Execute here.
        }
    }
}
