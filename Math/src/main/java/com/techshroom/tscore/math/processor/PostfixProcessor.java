package com.techshroom.tscore.math.processor;

import java.math.BigDecimal;
import java.util.LinkedList;

import com.techshroom.tscore.math.exceptions.EvalException;
import com.techshroom.tscore.math.exceptions.EvalException.Reason;
import com.techshroom.tscore.math.operator.Operator;
import com.techshroom.tscore.math.processor.token.*;

public class PostfixProcessor extends ExpressionProcessor {
    private final LinkedList<NumberToken> stack = new LinkedList<NumberToken>();
    protected BigDecimal result;

    protected PostfixProcessor(String proc) throws EvalException {
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
        if (t instanceof NumberToken) {
            stack.push((NumberToken) t);
        } else if (t instanceof BasicToken) {
            BasicToken bt = (BasicToken) t;
            if (bt.flag() == TokenFlag.OPERATOR) {
                Operator o = Operator.getOperator(bt.value());
                System.err.println("op proc on " + stack);
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
                throw new EvalException(Reason.UKNOWN_ERROR, "Can't handle "
                        + bt + " yet!");
            }
        }
    }

    @Override
    protected void callTokenize() {
        tokenize();
        generateResult();
    }

    protected void generateResult() {
        if (stack.size() != 1 || !(stack.peek() instanceof NumberToken)) {
            throw new EvalException(Reason.TOO_MANY_NUMBERS, "1", stack);
        }
        result = ((NumberToken) stack.pop()).getBigDecimal();
    }
}
