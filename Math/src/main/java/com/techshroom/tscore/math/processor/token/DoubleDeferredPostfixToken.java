package com.techshroom.tscore.math.processor.token;

import com.techshroom.tscore.math.processor.PostfixProcessor;

public class DoubleDeferredPostfixToken extends PostfixProcessor implements
        Token {
    public DoubleDeferredPostfixToken() {
    }

    @Override
    public String value() {
        return process().toString();
    }
}
