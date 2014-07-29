package com.techshroom.tscore.math.processor;

import java.math.BigDecimal;
import java.util.*;

import com.techshroom.tscore.math.exceptions.EvalException;
import com.techshroom.tscore.math.exceptions.EvalException.Reason;
import com.techshroom.tscore.math.processor.token.*;

public class InfixProcessor extends ExpressionProcessor {
    private static enum State {
        DEFAULT, WAITING_FOR_L_PAREN, WAITING_FOR_R_PAREN, BUILDING_COMMA_LIST;
    }

    protected final TokenProc worker = new TokenProc();

    protected BigDecimal result = BigDecimal.ZERO;

    protected InfixProcessor(String proc) throws EvalException {
        super(proc);
    }

    @Override
    public BigDecimal process() {
        callTokenize();
        return result;
    }

    @Override
    protected void onToken(Token t, int index) {
        worker.onToken(t);
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
        private final LinkedList<LinkedList<Token>> listOfBuildingList;
        private String argument;

        private TokenProc() {
            output = new LinkedList<Token>();
            operators = new LinkedList<Token>();
            listOfBuildingList = new LinkedList<LinkedList<Token>>();
        }

        private void onToken(Token t) {
            if (t == null) {
                throw new IllegalStateException("Null token");
            }

            if (state == State.WAITING_FOR_L_PAREN) {
                state = State.DEFAULT;
                if (t.value().equals("(")) {
                    // only functions wait for (, it is otherwise "unexpected"
                    state = State.BUILDING_COMMA_LIST;
                    listOfBuildingList.push(new LinkedList<Token>());
                    argument = "";
                    listOfBuildingList.peek().add(passToken);
                } else {
                    throw new EvalException(Reason.PARSE_ERROR, "(", "after "
                            + passToken.value() + "; before " + t.value());
                }
                passToken = null;
                return;
            }

            if (state == State.BUILDING_COMMA_LIST) {
                // this is sort of a waiting for R_PAREN state
                // but we are also building a list.
                if (t.value().equals(")")) {
                    finishedMakingArgument();
                    // list over, man
                    LinkedList<Token> list = listOfBuildingList.pop();
                    output.push(new ListToken(list));
                    return;
                } else if (t.value().equals(",")) {
                    finishedMakingArgument();
                } else {
                    argument += t.value();
                    return;
                }

                if (listOfBuildingList.isEmpty()) {
                    state = State.DEFAULT;
                }
            }

            if (t instanceof NumberToken) {
                output.add(t);
                return;
            }
            String tknstr = t.value();
            if (tknstr.matches(RegexBits.FUNCTION)) {
                state = State.WAITING_FOR_L_PAREN;
                passToken = t;
                return;
            } else if (tknstr.equals("(")) {
                operators.add(t);
            }else if (tknstr.equals(")")) {
                operators.add(t);
            } else {
                // some operator
            }
        }

        private void finishedMakingArgument() {
            Token argumentValue = new DeferredInfixToken(generateTokens(
                    argument, null));
            listOfBuildingList.peek().add(argumentValue);
        }
    }
}
