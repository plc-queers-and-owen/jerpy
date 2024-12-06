import java.util.ArrayList;

import internal.ExecutionException;
import internal.SemanticException;
import internal.nodes.ProgramNode;
import internal.scope.Scope;
import provided.JottParser;
import provided.JottTokenizer;
import provided.Token;

public class Jott {

    public static void main(String[] args) {
        Scope scope = new Scope();

        ArrayList<Token> tokens = JottTokenizer.tokenize(args[0]);
        if (tokens == null) {
            return;
        }
        ProgramNode program = (ProgramNode) JottParser.parse(tokens);
        if (program == null) {
            return;
        }
        if (program.validateTree(scope)) {
            try {
                program.execute(scope);
            } catch (SemanticException e) {
                e.report(program);
                e.printStackTrace();
            } catch (ExecutionException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
