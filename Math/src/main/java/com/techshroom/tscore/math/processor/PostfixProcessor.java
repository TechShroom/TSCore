package com.techshroom.tscore.math.processor;

import java.math.BigDecimal;
import java.util.LinkedList;

import com.techshroom.tscore.math.exceptions.EvalException;
import com.techshroom.tscore.math.exceptions.EvalException.Reason;
import com.techshroom.tscore.math.operator.*;
import com.techshroom.tscore.math.processor.token.*;

public class PostfixProcessor {
    private final LinkedList<NumberToken> stack = new LinkedList<NumberToken>();
    private Operator hold;
    protected BigDecimal result;

    protected PostfixProcessor() {
    }

    public final BigDecimal process() {
        if (result == null) {
            doTheHardWork();
        }
        return result;
    }

    protected void doTheHardWork() {
        callTokenize();
    }

    protected void onToken(Token t, int index) {
        if (t instanceof NumberToken) {
            stack.push((NumberToken) t);
        } else if (t instanceof BasicToken) {
            BasicToken bt = (BasicToken) t;
            if (bt.flag() == TokenFlag.OPERATOR) {
                Operator o = Operator.getOperator(bt.value());
                System.err.println(o + " proc on " + stack);
                if (o.getKey().getPlacement() == NumberPlacement.RIGHT) {
                    // special case: op is to the left, apply to the right
                    throw new EvalException(
                            Reason.UKNOWN_ERROR,
                            "Can't handle placement RIGHT in RPN, " + "it doesn't know "
                                    + "what placement is!");
                }
                if (o.inputCount() <= stack.size()) {
                    // enough values
                    BigDecimal[] args = new BigDecimal[o.inputCount()];
                    for (int i = 0; i < args.length; i++) {
                        args[i] = stack.pop().getBigDecimal();
                    }
                    BigDecimal res = o.runTheNumbers(args);
                    stack.push(new NumberToken(res));
                } else {
                    throw new EvalException(Reason.NO_NUMBERS);
                }
            } else {
                throw new EvalException(Reason.UKNOWN_ERROR,
                        "Can't handle " + bt + " yet!");
            }
        }
    }

    protected void callTokenize() {
        // tokenize();
        generateResult();
    }

    protected void generateResult() {
        if (stack.size() != 1 || !(stack.peek() instanceof NumberToken)) {
            throw new EvalException(Reason.TOO_MANY_NUMBERS, "1", stack);
        }
        result = ((NumberToken) stack.pop()).getBigDecimal();
    }
}
