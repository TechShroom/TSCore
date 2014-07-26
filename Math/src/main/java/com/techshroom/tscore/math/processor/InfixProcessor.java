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

    private final TokenProc worker = new TokenProc();

    protected InfixProcessor(String proc) throws EvalException {
        super(proc);
    }

    @Override
    public BigDecimal process() {
        callTokenize();
        return null;
    }

    @Override
    protected void onToken(Token t) {
        worker.onToken(t);
    }

    @Override
    protected void callTokenize() {
        tokenize();
    }

    private final class TokenProc {
        private State state = State.DEFAULT;
        private Token passToken;
        private final LinkedList<Token> output;
        private final LinkedList<Token> operators;
        private final LinkedList<List<Token>> listOfBuildingList;

        private TokenProc() {
            output = new LinkedList<Token>();
            operators = new LinkedList<Token>();
            listOfBuildingList = new LinkedList<List<Token>>();
        }

        private void onToken(Token t) {
            if (t == null) {
                throw new IllegalStateException("Null token");
            }

            if (state == State.WAITING_FOR_L_PAREN) {
                passToken = null;
                state = State.DEFAULT;
                if (t.value().equals("(")) {
                    // only functions wait for (, it is otherwise "unexpected"
                    state = State.BUILDING_COMMA_LIST;
                    listOfBuildingList.push(new ArrayList<Token>());
                } else {
                    throw new EvalException(Reason.PARSE_ERROR, "(", "after "
                            + passToken.value() + "; before " + t.value());
                }
                return;
            }

            if (state == State.BUILDING_COMMA_LIST) {
                // this is sort of a waiting for R_PAREN state
                // but we are also building a list.
                if (t.value().equals(")")) {
                    // list over, man
                    output.push(new ListToken(listOfBuildingList.pop()));
                } else if (t.value().equals(",")) {
                    
                }
            }

            if (t instanceof NumberToken) {
                output.add(t);
                return;
            }
            String tknstr = t.value();
            if (tknstr.length() > 1 && tknstr.matches(RegexBits.FUNCTION)) {
                state = State.WAITING_FOR_L_PAREN;
                passToken = t;
            }
        }
    }
}
