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

import static com.techshroom.tscore.util.QuickStringBuilder.*;

public class BasicToken implements Token {
    private final String value;
    private final TokenFlag flag;

    public BasicToken(String val, TokenFlag flg) {
        value = val;
        flag = flg;
    }

    @Override
    public String value() {
        return value;
    }

    public TokenFlag flag() {
        return flag;
    }

    @Override
    public int hashCode() {
        return value.hashCode() - flag.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof BasicToken) {
            return ((BasicToken) obj).value.equals(value) && ((BasicToken) obj).flag
                    .equals(flag);
        }
        return false;
    }

    @Override
    public String toString() {
        return concatToStringComplex(flag, "<", "value=", value, ">");
    }
}
