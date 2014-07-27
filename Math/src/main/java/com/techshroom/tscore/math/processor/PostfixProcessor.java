package com.techshroom.tscore.math.processor;

import java.math.BigDecimal;

import com.techshroom.tscore.math.exceptions.EvalException;
import com.techshroom.tscore.math.processor.token.Token;

public class PostfixProcessor extends ExpressionProcessor {

    protected BigDecimal result = BigDecimal.ZERO;

    protected PostfixProcessor(String proc) throws EvalException {
        super(proc);
    }

    @Override
    public BigDecimal process() {
        callTokenize();
        return null;
    }

    @Override
    protected void onToken(Token t, int index) {
    }

    @Override
    protected void callTokenize() {
        tokenize();
    }
}
