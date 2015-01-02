/**
 * Copyright (c) 2014 TechShroom
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.techshroom.tscore.math.processor;

import static com.techshroom.tscore.util.QuickStringBuilder.*;

import java.math.BigDecimal;
import java.util.*;

import com.techshroom.tscore.math.exceptions.EvalException;
import com.techshroom.tscore.math.exceptions.EvalException.Reason;
import com.techshroom.tscore.math.operator.*;
import com.techshroom.tscore.math.processor.token.*;

public class InfixProcessor extends ExpressionProcessor {
    private static enum State {
        DEFAULT, WAITING_FOR_L_PAREN, WAITING_FOR_R_PAREN, WAITING_FOR_LIST,
        /**
         * {@link NumberPlacement#RIGHT} RPN avoidance helper.
         */
        WAITING_FOR_PLACERIGHT_ARG,
        /**
         * Last token was an operator, detect {@link NumberPlacement#RIGHT} ops
         * next.
         */
        __NOTE_LAST_OP;
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
        protected Deque<State> state = new LinkedList<State>(
                Arrays.asList(State.DEFAULT));
        protected Token passToken;
        protected TokenProc creatingNPRArg = null;
        protected final LinkedList<Token> output;
        protected final LinkedList<Token> operators;
        protected final List<Token> funcList;

        protected TokenProc() {
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
                    output.add(t);
                }
            }
        }

        protected void onToken(Token t, int index) {
            if (t == null) {
                throw new IllegalStateException("Null token");
            }

            if (state.peek() == State.WAITING_FOR_PLACERIGHT_ARG) {
                // number || function -> resolve to number ||
                // op+number -> resolve to number
                return;
            }

            if (state.peek() == State.WAITING_FOR_L_PAREN) {
                state.pop();
                if (t.value().equals("(")) {
                    // only functions wait for (, it is otherwise "unexpected"
                    state.push(State.WAITING_FOR_LIST);
                } else {
                    throw new EvalException(Reason.PARSE_ERROR, "(",
                            "after " + passToken.value()
                                    + "; before "
                                    + t.value());
                }
                passToken = null;
                return;
            } else if (state.peek() == State.WAITING_FOR_LIST) {
                state.pop();
                if (t instanceof ListToken) {
                    // args list
                    funcList.add(t);
                    state.push(State.WAITING_FOR_R_PAREN);
                } else {
                    throw new EvalException(Reason.PARSE_ERROR,
                            "a list of args", Integer.valueOf(index));
                }
                return;
            } else if (state.peek() == State.WAITING_FOR_R_PAREN) {
                state.pop();
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
                state.push(State.WAITING_FOR_L_PAREN);
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
                Operator o1 = Operator.getOperator(t.value(),
                        NumberPlacement.BOTH);
                if (state.peek() == State.__NOTE_LAST_OP) {
                    o1 = Operator.getOperator(t.value(), NumberPlacement.RIGHT);
                    if (o1 != null) {
                        state.push(State.WAITING_FOR_PLACERIGHT_ARG);
                        creatingNPRArg = new TokenProc();
                        operators.push(t);
                    } else {
                        throw new EvalException(Reason.UKNOWN_ERROR,
                                "Operator was not " + "a RIGHT placement op");
                    }
                    return;
                }
                if (state.peek() != State.__NOTE_LAST_OP) {
                    state.push(State.__NOTE_LAST_OP);
                }
                while (operators.peek() != null) {
                    Operator o2 = Operator.getOperator(
                            operators.peek().value(), NumberPlacement.BOTH);
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
