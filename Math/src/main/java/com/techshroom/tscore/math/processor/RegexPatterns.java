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

import static com.techshroom.tscore.math.processor.RegexBits.*;

import java.util.regex.Pattern;

public final class RegexPatterns {
    /**
     * Regex to find any numbers in a string.
     */
    public static final Pattern NUMBER_REGEX = Pattern.compile(NUMBER_BASE);
    /**
     * Regex to find functions.
     * */
    public static final Pattern FUNCTION_GET_REGEX = Pattern
            .compile(FUNCTION + LPAREN + ".*?" + RPAREN);
    /**
     * Regex to find function define statements.
     * */
    public static final Pattern FUNCTION_DEF_GET_REGEX = Pattern
            .compile(FUNCTION + LPAREN + "(?:" + OPTSPACE + ")" + RPAREN);
    /**
     * Regex to find variables.
     * */
    public static final Pattern VARIABLE_GET_REGEX = Pattern.compile(VARIABLE);
    /**
     * Regex to get operators.
     */
    public static final Pattern OPERATOR_GET_REGEX = Pattern.compile(OPERATOR);
    /**
     * Regex to get strings.
     */
    public static final Pattern STRING_GET_REGEX = Pattern.compile("\"(.+?)\"");

    private RegexPatterns() {
    }
}
