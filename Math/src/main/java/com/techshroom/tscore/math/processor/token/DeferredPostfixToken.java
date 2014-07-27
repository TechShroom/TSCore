package com.techshroom.tscore.math.processor.token;

import java.math.BigDecimal;
import java.util.List;

import com.techshroom.tscore.math.processor.InfixProcessor;
import com.techshroom.tscore.math.processor.PostfixProcessor;

public class DeferredPostfixToken extends PostfixProcessor implements Token {
    private final List<Token> values;

    public DeferredPostfixToken(List<Token> val) {
        // string value not used
        super(val.toString());
        values = val;
    }

    @Override
    public String value() {
        return process().toString();
    }

    @Override
    public BigDecimal process() {
        doTokenize();
        return result;
    }

    private void doTokenize() {
        for (Token t : values) {
            onToken(t, -1);
        }
    }

    @Override
    public String toString() {
        return values.toString();
    }

    @Override
    public int hashCode() {
        return values.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof DeferredPostfixToken) {
            DeferredPostfixToken other = (DeferredPostfixToken) obj;
            return values.equals(other.values);
        }
        return true;
    }
}
