package com.techshroom.tscore.math.processor;

import static com.techshroom.tscore.util.QuickStringBuilder.*;

import java.math.BigDecimal;
import java.util.*;

import com.techshroom.tscore.math.exceptions.EvalException;
import com.techshroom.tscore.math.exceptions.EvalException.Reason;
import com.techshroom.tscore.math.operator.Associativeness;
import com.techshroom.tscore.math.operator.Operator;
import com.techshroom.tscore.math.processor.token.*;

public class InfixProcessor extends ExpressionProcessor {
    private static enum State {
        DEFAULT, WAITING_FOR_L_PAREN, WAITING_FOR_R_PAREN, WAITING_FOR_LIST;
    }

    protected final TokenProc worker = new TokenProc();

    protected BigDecimal result;

    protected InfixProcessor(String proc) throws EvalException {
        super(proc);
    }

    @Override
    public final BigDecimal process() {
        if (result == null) {
            doTheHardWork();
        }
        return result;
    }

    protected void doTheHardWork() {
        callTokenize();
    }

    @Override
    protected void onToken(Token t, int index) {
        worker.onToken(t, index);
    }

    @Override
    protected void callTokenize() {
        tokenize();
        generateResult();
    }

    protected void generateResult() {
        worker.finish();
        List<Token> copy = new ArrayList<Token>(worker.output);
        // Collections.reverse(copy);
        result = new DeferredPostfixToken(copy).process();
        System.err.println(worker.output + " from " + Thread.currentThread());
    }

    protected final class TokenProc {
        private State state = State.DEFAULT;
        private Token passToken;
        private final LinkedList<Token> output;
        private final LinkedList<Token> operators;
        private final List<Token> funcList;

        private TokenProc() {
            output = new LinkedList<Token>();
            operators = new LinkedList<Token>();
            funcList = new ArrayList<Token>();
        }

        protected void finish() {
            System.err.println("Worker: Pop ops-> " + operators);
            while (!operators.isEmpty()) {
                Token t = operators.pop();
                if (t instanceof BasicToken) {
                    BasicToken bt = (BasicToken) t;
                    if (bt.flag() == TokenFlag.OTHER && bt.value().equals("(")) {
                        throw new EvalException(Reason.PARSE_ERROR, "(",
                                "before EOL");
                    } else {
                        output.add(t);
                    }
                } else {
                    output.push(t);
                }
            }
        }

        private void onToken(Token t, int index) {
            if (t == null) {
                throw new IllegalStateException("Null token");
            }

            if (state == State.WAITING_FOR_L_PAREN) {
                if (t.value().equals("(")) {
                    // only functions wait for (, it is otherwise "unexpected"
                    state = State.WAITING_FOR_LIST;
                } else {
                    state = State.DEFAULT;
                    throw new EvalException(Reason.PARSE_ERROR, "(",
                            "after " + passToken.value()
                                    + "; before "
                                    + t.value());
                }
                passToken = null;
                return;
            } else if (state == State.WAITING_FOR_LIST) {
                if (t instanceof ListToken) {
                    // args list
                    funcList.add(t);
                    state = State.WAITING_FOR_R_PAREN;
                } else {
                    state = State.DEFAULT;
                    throw new EvalException(Reason.PARSE_ERROR,
                            "a list of args", Integer.valueOf(index));
                }

                if (funcList.isEmpty()) {
                    state = State.DEFAULT;
                }
                return;
            } else if (state == State.WAITING_FOR_R_PAREN) {
                state = State.DEFAULT;
                if (t.value().equals(")")) {
                    output.push(new ListToken(funcList));
                    funcList.clear();
                    return;
                } else {
                    throw new EvalException(Reason.PARSE_ERROR, ")",
                            Integer.valueOf(index));
                }
            }

            if (t instanceof NumberToken) {
                output.add(t);
                return;
            }
            if (!(t instanceof BasicToken)) {
                // hmmm...
                state = State.DEFAULT;
                throw new RuntimeException(
                        "Token not a BasicToken," + " ask dev to determine what should "
                                + "happen here. Sorry! "
                                + t);
            }
            BasicToken bt = (BasicToken) t;
            handleBasicTokenStuff(bt, index);
        }

        private void handleBasicTokenStuff(BasicToken t, int index) {
            String tknstr = t.value();
            if (t.flag() == TokenFlag.FUNCTION) {
                state = State.WAITING_FOR_L_PAREN;
                passToken = t;
                return;
            } else if (t.flag() == TokenFlag.OTHER) {
                if (tknstr.equals("(")) {
                    operators.push(t);
                } else if (tknstr.equals(")")) {
                    while (!operators.isEmpty() && !operators.peek().value()
                            .equals("(")) {
                        output.push(operators.pop());
                    }
                    if (operators.isEmpty()) {
                        throw new EvalException(Reason.PARSE_ERROR, "(",
                                "somewhere before ) at " + Integer
                                        .valueOf(index));
                    }
                    operators.pop();
                } else {
                    throw new EvalException(Reason.PARSE_ERROR, "( or )",
                            Integer.valueOf(index));
                }
            } else if (t.flag() == TokenFlag.OPERATOR) {
                // some operator
                Operator o1 = Operator.getOperator(t.value());
                while (operators.peek() != null) {
                    Operator o2 = Operator
                            .getOperator(operators.peek().value());
                    System.err.println(concatToString("checking ", o2, " aka ",
                            o2.getKey()));
                    Associativeness a1 = o1.getKey().getAssociativeness();
                    if (a1 == Associativeness.LEFT) {
                        if (Operator.PRIORITY_COMPARATOR.compare(o1, o2) <= 0) {
                            output.push(operators.pop());
                        } else {
                            break;
                        }
                    } else if (a1 == Associativeness.RIGHT) {
                        if (Operator.PRIORITY_COMPARATOR.compare(o1, o2) < 0) {
                            output.push(operators.pop());
                        } else {
                            break;
                        }
                    } else {
                        throw new EvalException(Reason.OPERATOR_ERROR,
                                o1 + " (not associative)");
                    }
                }
                System.err.println(concatToString("Pushing ", t, " onto ",
                        operators));
                operators.push(t);
            } else if (t.flag() == TokenFlag.FUNCTION_DEF) {
                // function def
            } else {
                throw new EvalException(Reason.UKNOWN_ERROR,
                        "Invalid token: " + t
                                + concatToString(" (state: ", state,
                                        ", output=", output, ", operators=",
                                        operators));
            }
        }
    }
}
