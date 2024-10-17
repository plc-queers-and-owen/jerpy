package provided;

import internal.ParseHaltException;

/**
 * This class is responsible for paring Jott Tokens
 * into a Jott parse tree.
 *
 * @author
 */

import internal.PeekingArrayIterator;
import internal.nodes.ProgramNode;

import java.util.ArrayList;

public class JottParser {

    /**
     * Parses an ArrayList of Jotton tokens into a Jott Parse Tree.
     * @param tokens the ArrayList of Jott tokens to parse
     * @return the root of the Jott Parse Tree represented by the tokens.
     *         or null upon an error in parsing.
     */
    public static JottTree parse(ArrayList<Token> tokens) {
        PeekingArrayIterator it = new PeekingArrayIterator(tokens);
        try {
            return ProgramNode.parse(it);
        } catch (ParseHaltException e) {
            System.err.println("Syntax Error");
            System.err.println(e.getMessage());
            System.err.println(it.getCurrentFilename() + ":" + it.getCurrentLine());
            return null;
        }
    }
}
