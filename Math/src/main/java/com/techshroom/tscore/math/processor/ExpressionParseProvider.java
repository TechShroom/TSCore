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
package com.techshroom.tscore.math.processor;

import java.util.ArrayList;
import java.util.List;

import com.techshroom.tscore.util.*;

public final class ExpressionParseProvider {
    public static final GetterKey<String> EXPRESSION_KEY = new GetterKey<String>(
            "expr");
    private static final List<MatchCheckAndGet<String, ? extends ExpressionProcessor>> factories = new ArrayList<MatchCheckAndGet<String, ? extends ExpressionProcessor>>();

    private ExpressionParseProvider() {
    }

    public static ExpressionProcessor processorFor(String expression) {
        for (MatchCheckAndGet<String, ? extends ExpressionProcessor> fact : factories) {
            if (fact.matches(expression)) {
                fact.setData(EXPRESSION_KEY, expression);
                return fact.get();
            }
        }
        return null;
    }

    public static <T extends ExpressionProcessor> void registerProcessor(
            MatchCheckAndGet<String, T> factory) {
        factories.add(factory);
    }

    static {
        // our procs
        registerProcessor(new SimpleMatchCheckAndGet<String, InfixProcessor>(
                new MatchChecker<String>() {
                    @Override
                    public boolean matches(String t) {
                        return /* TODO: detect diff btwn post/in fix */true;
                    }
                }, new SimpleGetter<InfixProcessor, String>() {
                    @Override
                    protected InfixProcessor doGet() {
                        return new InfixProcessor(getARG());
                    }

                    @Override
                    public <X> void setData(GetterKey<X> key, X value) {
                        if (key == EXPRESSION_KEY) {
                            super.setData(key(), EXPRESSION_KEY.cast(value));
                        }
                    }
                }));
    }
}
