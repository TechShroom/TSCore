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
