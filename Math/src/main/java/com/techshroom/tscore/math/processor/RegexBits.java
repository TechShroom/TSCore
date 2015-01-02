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

public final class RegexBits {
    /**
     * Parenthesis.
     */
    public static final String LPAREN = "\\(", RPAREN = "\\)";
    /**
     * Variable finder in the form @variableName, where variableName may contain
     * alpha-numeric values only and must not start with a number.
     */
    public static final String VARIABLE = "(@[a-zA-Z][a-zA-Z0-9]*)";
    /**
     * Function finder in the form functionName, where functionName may contain
     * alpha-numeric values only and must not start with a number.
     */
    public static final String FUNCTION = "([a-zA-Z][a-zA-Z0-9]*)";
    /**
     * Optional space matcher that is not included in the output
     */
    public static final String OPTSPACE = "(?:\\s*)";
    /**
     * Number finder (float). Includes `E` matching and negative numbers.
     */
    public static final String NUMBER_BASE = "((\\d+(?:\\.\\d+)?)" + "(?:[eE]([-+]?\\d+))?)";
    /**
     * Operator regex to find just the operator
     */
    public static final String OPERATOR = "([^a-zA-Z0-9 ]+)";

    private RegexBits() {
    }
}