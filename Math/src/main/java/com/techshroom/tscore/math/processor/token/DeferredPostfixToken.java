package com.techshroom.tscore.math.processor.token;

import java.util.List;

import com.techshroom.tscore.math.processor.PostfixProcessor;

public class DeferredPostfixToken extends PostfixProcessor implements Token {
    private final List<Token> values;

    public DeferredPostfixToken(List<Token> val) {
        // string value not used
        super(val.toString());
        values = val;
        System.err.println(toString());
    }

    @Override
    public String value() {
        return process().toString();
    }

    @Override
    public void doTheHardWork() {
        doTokenize();
        generateResult();
    }

    private void doTokenize() {
        for (Token t : values) {
            super_onToken(t, -1);
        }
    }

    private void super_onToken(Token t, int index) {
        super.onToken(t, index);
    }

    @Override
    protected void onToken(Token t, int index) {
        throw new UnsupportedOperationException(
                "A defered parser cannot accept more tokens");
    }

    @Override
    public String toString() {
        return values.toString() + " (Deferred)";
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
