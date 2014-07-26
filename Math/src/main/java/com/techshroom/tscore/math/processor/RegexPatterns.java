package com.techshroom.tscore.math.processor;

import java.util.regex.Pattern;

public final class RegexPatterns {
    /**
     * Regex to find any numbers in a string.
     */
    public static final Pattern NUMBER_REGEX = Pattern
            .compile(RegexBits.NUMBER_BASE);
    /**
     * Regex to validate functions.
     */
    public static final Pattern FUNCTION_VALIDATE_REGEX = Pattern.compile("("
            + RegexBits.FUNCTION + RegexBits.OPTSPACE + "\\(("
            + RegexBits.OPTSPACE + RegexBits.VARIABLE + "?(?:"
            + RegexBits.OPTSPACE + "," + RegexBits.OPTSPACE
            + RegexBits.VARIABLE + ")*)" + RegexBits.OPTSPACE + "\\))");
    /**
     * Regex to find functions.
     * */
    public static final Pattern FUNCTION_GET_REGEX = Pattern.compile("("
            + RegexBits.FUNCTION + RegexBits.OPTSPACE + "(?=\\(("
            + RegexBits.OPTSPACE + RegexBits.NUMBER_BASE + "?(?:"
            + RegexBits.OPTSPACE + "," + RegexBits.OPTSPACE
            + RegexBits.NUMBER_BASE + ")*)" + RegexBits.OPTSPACE + "\\)))");
    /**
     * Regex to get operators.
     */
    public static final Pattern OPERATOR_GET_REGEX = Pattern
            .compile(RegexBits.OPERATOR);

    private RegexPatterns() {
    }
}
