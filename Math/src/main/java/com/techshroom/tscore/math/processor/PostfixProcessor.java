package com.techshroom.tscore.math.processor;

import java.math.BigDecimal;

import com.techshroom.tscore.math.exceptions.EvalException;
import com.techshroom.tscore.math.processor.token.Token;

public class PostfixProcessor extends ExpressionProcessor {

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
        //
    }

    @Override
    protected void callTokenize() {
        tokenize();
    }
}
