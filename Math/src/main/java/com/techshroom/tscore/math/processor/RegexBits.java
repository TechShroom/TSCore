package com.techshroom.tscore.math.processor;

public final class RegexBits {
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
    public static final String NUMBER_BASE = "((\\d+(?:\\.\\d+)?)"
            + "(?:[eE]([-+]?\\d+))?)";
    /**
     * Operator regex to find just the operator
     */
    public static final String OPERATOR = "([^a-zA-Z0-9])";

    private RegexBits() {
    }
}