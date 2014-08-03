package com.techshroom.tscore.math.processor.token;

import com.techshroom.tscore.math.processor.PostfixProcessor;

public class DoubleDeferredPostfixToken extends PostfixProcessor implements
        Token {
    public DoubleDeferredPostfixToken(String val) {
        super(val);
    }

    @Override
    public String value() {
        return process().toString();
    }
}
