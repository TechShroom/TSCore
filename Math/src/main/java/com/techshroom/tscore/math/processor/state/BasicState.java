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
package com.techshroom.tscore.math.processor.state;

import java.util.*;

import com.techshroom.tscore.math.exceptions.EvalException;
import com.techshroom.tscore.math.exceptions.EvalException.Reason;

public class BasicState {
    @SuppressWarnings("unchecked")
    private static final <T> T noticeCast(Object o) {
        return (T) o;
    }

    /**
     * The variable storage, since functions <i>are</i> variables this holds
     * them too.
     */
    private final Map<String, Variable<?>> vars = new HashMap<String, Variable<?>>();

    public BasicState() {
    }

    public void addVars(Variable<?>... vrs) {
        for (Variable<?> v : vrs) {
            if (vars.containsKey(v.name())) {
                throw new EvalException(Reason.DUPLICATE, v.name(), vars.get(v
                        .name()), v);
            }
            vars.put(v.name(), v);
        }
    }

    public <T> Variable<T> lookupVar(String name) {
        Variable<?> check = vars.get(name);
        if (check == null) {
            return null;
        }
        return noticeCast(check);
    }

    public void addFunctions(Function<?>... fncs) {
        List<Variable<Function<?>>> tmp = new ArrayList<Variable<Function<?>>>();
        for (Function<?> f : fncs) {
            if (vars.containsKey(f.name())) {
                throw new EvalException(Reason.DUPLICATE, f.name() + "()",
                        vars.get(f.name()), f);
            }
            tmp.add(new Variable<Function<?>>(f.name(), f));
        }
        addVars(tmp.toArray(new Variable<?>[0]));
    }

    public <T, X extends Function<T>> Variable<X> lookupFunctionAsVar(
            String name) {
        Variable<?> check = vars.get(name);
        if (check == null) {
            return null;
        }
        return noticeCast(check);
    }

    public <T, X extends Function<T>> X lookupFunction(String name) {
        return this.<T, X> lookupFunctionAsVar(name).data();
    }
}
