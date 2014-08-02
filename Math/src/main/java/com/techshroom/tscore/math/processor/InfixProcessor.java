package com.techshroom.tscore.math.processor;

import static com.techshroom.tscore.util.QuickStringBuilder.*;

import java.math.BigDecimal;
import java.util.*;

import com.techshroom.tscore.math.exceptions.EvalException;
import com.techshroom.tscore.math.exceptions.EvalException.Reason;
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
        result = new DeferredPostfixToken(worker.output).process();
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
                    throw new EvalException(Reason.PARSE_ERROR, "(", "after "
                            + passToken.value() + "; before " + t.value());
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
                throw new RuntimeException("Token not a BasicToken,"
                        + " ask dev to determine what should "
                        + "happen here. Sorry! " + t);
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
                    operators.add(t);
                } else if (tknstr.equals(")")) {
                    operators.add(t);
                } else {
                    throw new EvalException(Reason.PARSE_ERROR, "( or )",
                            Integer.valueOf(index));
                }
            } else if (t.flag() == TokenFlag.OPERATOR) {
                // some operator
                while (operators.peek() != null) {
                    Operator pop = Operator
                            .getOperator(operators.pop().value());
                }
            } else if (t.flag() == TokenFlag.FUNCTION_DEF) {
                // function def
            } else {
                throw new EvalException(Reason.UKNOWN_ERROR, "Invalid token: "
                        + t
                        + concatToString(" (state: ", state, ", output=",
                                output, ", operators=", operators));
            }
        }
    }
}
