package com.techshroom.tscore.math.exceptions;

public class EvalException extends RuntimeException {
    public static enum Reason {
        /**
         * No numbers to eval. Args: []
         */
        NO_NUMBERS("No numbers were provided"),
        /**
         * Too many numbers to eval. Args: [expected, found]
         */
        TOO_MANY_NUMBERS("Expected %s numbers but found %s"),
        /**
         * No operator found. Args: [operator]
         */
        NO_SUCH_OPERATOR("No such operator %s"),
        /**
         * No function found. Args: [function_name]
         */
        NO_SUCH_FUNCTION("No such function %s"),
        /**
         * Needed an operator. Args: []
         */
        NEEDED_OPERATOR("Needed an operator."),
        /**
         * Needed a function. Args: []
         */
        NEEDED_FUNCTION("Needed a function."),
        /**
         * Unexpected error. Args: [expected_char, index_or_str]
         */
        PARSE_ERROR("Unexpected error while reading string, expected %s @ %s"),
        /**
         * Operator failed during processing. Args: [operator]
         */
        OPERATOR_ERROR("Operator %s threw an error while being processed"),
        /**
         * Function failed during processing. Args: [function_name]
         */
        FUNCTION_ERROR("Function %s threw an error while being processed"),
        /**
         * Unknown error. Args: [message]
         */
        UKNOWN_ERROR("Unknown error %s");

        private final String message;

        private Reason(String msg) {
            message = msg;
        }

        public String formmated(Object... args) {
            return String.format(message, args);
        }

        public String unformmated() {
            return message;
        }
    }

    private static final long serialVersionUID = -987635654396074492L;

    private final Reason reason;

    public EvalException(String message) {
        this(Reason.UKNOWN_ERROR, message);
    }

    public EvalException(Reason r) {
        super(r.unformmated());
        reason = r;
    }

    public EvalException(Reason r, Object... args) {
        super(r.formmated(args));
        reason = r;
    }

    public EvalException(Reason r, Throwable cause) {
        super(r.unformmated(), cause);
        reason = r;
    }

    public EvalException(Reason r, Throwable cause, Object... args) {
        super(r.formmated(args), cause);
        reason = r;
    }

    public Reason getReason() {
        return reason;
    }
}
