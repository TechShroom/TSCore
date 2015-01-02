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
package com.techshroom.tscore.util;

public abstract class SimpleGetter<T, ARG> implements Getter<T> {
    private ARG arg;
    private boolean argSetFlag = false;
    private final GetterKey<ARG> argKey = new GetterKey<ARG>("arg");

    @Override
    public <X> void setData(GetterKey<X> key, X value) {
        if (key == argKey) {
            arg = argKey.cast(value);
            argSetFlag = true;
        }
    }

    @Override
    public final T get() {
        if (!argSetFlag) {
            handleUnsetArg();
        }
        return doGet();
    }

    protected abstract T doGet();

    @SuppressWarnings("static-method")
    protected void handleUnsetArg() {
        throw new IllegalStateException("must set argument");
    }

    protected ARG getARG() {
        return arg;
    }

    public GetterKey<ARG> key() {
        return argKey;
    }
}
