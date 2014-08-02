package com.techshroom.tscore.math.processor.token;

import com.techshroom.tscore.math.processor.InfixProcessor;

public class DoubleDeferredInfixToken extends InfixProcessor implements Token {
    public DoubleDeferredInfixToken(String val) {
        super(val);
    }

    @Override
    public String value() {
        return process().toString();
    }
}
