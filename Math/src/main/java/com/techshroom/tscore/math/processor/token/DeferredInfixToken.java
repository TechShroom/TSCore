/**
 * Copyright (c) 2014 TechShroom
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.techshroom.tscore.math.processor.token;

import java.util.ArrayList;
import java.util.List;

import com.techshroom.tscore.math.processor.InfixProcessor;

public class DeferredInfixToken extends InfixProcessor implements Token {
    private final List<Token> values;
    private int ind = 0;

    public DeferredInfixToken(List<Token> val) {
        // string value not used
        super(val.toString());
        values = new ArrayList<Token>(val);
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
            super_onToken(t, ind);
            ind += t.value().length();
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
