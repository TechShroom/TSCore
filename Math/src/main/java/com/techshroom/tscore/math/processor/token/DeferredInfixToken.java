package com.techshroom.tscore.math.processor.token;

import java.math.BigDecimal;
import java.util.List;

import com.techshroom.tscore.math.processor.InfixProcessor;

public class DeferredInfixToken extends InfixProcessor implements Token {
    private final List<Token> values;

    public DeferredInfixToken(List<Token> val) {
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
        generateResult();
        return result;
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
                "A deffered parser cannot accept more tokens");
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
        if (obj instanceof DeferredInfixToken) {
            DeferredInfixToken other = (DeferredInfixToken) obj;
            return values.equals(other.values);
        }
        return true;
    }
}
