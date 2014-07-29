package com.techshroom.tscore.math.processor;

import java.util.regex.Pattern;

public final class RegexPatterns {
    /**
     * Regex to find any numbers in a string.
     */
    public static final Pattern NUMBER_REGEX = Pattern
            .compile(RegexBits.NUMBER_BASE);
    /**
     * Regex to find functions.
     * */
    public static final Pattern FUNCTION_GET_REGEX = Pattern
            .compile(RegexBits.FUNCTION);
    /**
     * Regex to get operators.
     */
    public static final Pattern OPERATOR_GET_REGEX = Pattern
            .compile(RegexBits.OPERATOR);
    /**
     * Regex to get strings.
     */
    public static final Pattern STRING_GET_REGEX = Pattern.compile("\"(.+?)\"");

    private RegexPatterns() {
    }
}
