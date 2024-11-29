package internal.nodes;

import java.util.Arrays;
import java.util.List;

import internal.ExecutionException;
import internal.ParseHaltException;
import internal.ParseUnexpectedTokenException;
import internal.PeekingArrayIterator;
import internal.SemanticException;
import internal.SemanticTypeException;
import internal.eval.Type;
import internal.eval.TypedValue;
import internal.scope.Scope;
import provided.Token;
import provided.TokenType;

/**
 * An expression which performs an operation on two operands
 * Node: <rel_op> | <math_op>
 */
public class BiOpExprNode extends ExprNode {
    private static final List<String> relOps = Arrays.asList("<", ">", "<=", ">=", "==", "!=");
    private final OperandNode a, b;
    private final String op;

    protected BiOpExprNode(String filename, int lineNumber, OperandNode a, String op, OperandNode b) {
        super(filename, lineNumber);
        this.a = a;
        this.op = op;
        this.b = b;
        this.adopt();
    }

    public static BiOpExprNode parse(PeekingArrayIterator it, OperandNode a)
            throws ParseUnexpectedTokenException, ParseHaltException {
        Token op = it.expect(TokenType.REL_OP, TokenType.MATH_OP);
        OperandNode b = OperandNode.parse(it);
        return new BiOpExprNode(it.getCurrentFilename(), op.getLineNum(), a, op.getToken(), b);
    }

    @Override
    public String convertToJott() {
        return a.convertToJott() + " " + op + " " + b.convertToJott();
    }

    @Override
    public boolean validateTree(Scope scope) {
        try {
            if (!this.a.inferType(scope).isNumeric()) {
                new SemanticException("Type Error: Expected either an Integer or Double value but got "
                        + this.a.inferType(scope).toString()).report(a);
                return false;
            }
            if (this.a.inferType(scope) != this.b.inferType(scope)) {
                new SemanticTypeException(this.b.inferType(scope), this.a.inferType(scope)).report(b);
                return false;
            }
            return true;
        } catch (SemanticException e) {
            e.report(this);
            return false;
        }

    }

    @Override
    public TypedValue evaluate(Scope scope) throws ExecutionException, SemanticException {
        double first = this.a.evaluate(scope).getNumericOperand();
        double second = this.b.evaluate(scope).getNumericOperand();
        TypedValue result;

        switch (this.op) {
            case "+":
                result = new TypedValue(first + second);
                break;
            case "-":
                result = new TypedValue(first + second);
                break;
            case "*":
                result = new TypedValue(first * second);
                break;
            case "/":
                result = new TypedValue(first / second);
                break;
            case "<":
                result = new TypedValue(first < second);
                break;
            case ">":
                result = new TypedValue(first > second);
                break;
            case "<=":
                result = new TypedValue(first <= second);
                break;
            case ">=":
                result = new TypedValue(first >= second);
                break;
            case "==":
                result = new TypedValue(first == second);
                break;
            case "!=":
                result = new TypedValue(first != second);
                break;
            default:
                throw new ExecutionException("Missed an unknown operator", this);
        }

        if (this.inferType(scope) == Type.Integer) {
            int resultInt = (int) Math.floor(result.getDouble());
            return new TypedValue(resultInt);
        } else {
            return result;
        }
    }

    @Override
    public List<Node> getChildren() {
        return List.of(this.a, this.b);
    }

    public boolean isRelOp() {
        return relOps.contains(this.op);
    }

    @Override
    public Type inferType(Scope scope) throws SemanticException {
        if (this.validateTree(scope)) {
            if (this.isRelOp()) {
                return Type.Boolean;
            } else {
                return this.a.inferType(scope);
            }
        }
        throw new SemanticException("Type Error: Type mismatch in numeric operation.");
    }
}
