package com.techshroom.tscore.util;

import java.util.Arrays;

public final class QuickStringBuilder {
    private static final String[] BUILD_DEFAULT_ENDSWITH = "=|->|:"
            .split("\\|");

    private QuickStringBuilder() {
    }

    /**
     * Makes a string from the result of concatenating the given arguments.
     * 
     * @param args
     *            - the input
     * @return the resultant string
     */
    public static final String concatToString(Object... args) {
        StringBuilder sb = new StringBuilder();
        for (Object o : args) {
            sb.append(o);
        }
        return sb.toString();
    }

    /**
     * Makes a string from the result of concatenating the given arguments. Adds
     * some special additions and converts arrays to strings properly.
     * 
     * @param args
     *            - the input
     * @return the resultant string
     */
    public static final String concatToStringComplex(Object... args) {
        StringBuilder sb = new StringBuilder();
        boolean addQuotes = false;
        for (Object o : args) {
            boolean strBfrConv = o instanceof String;
            if (strBfrConv && addQuotes) {
                sb.append("'");
            }
            o = convert(o);
            sb.append(o);
            if (strBfrConv) {
                if (addQuotes) {
                    sb.append("'");
                } else {
                    addQuotes = endsWith_array((String) o,
                            BUILD_DEFAULT_ENDSWITH);
                    continue;
                }
            }
            addQuotes = false;
        }
        return sb.toString();
    }

    private static Object convert(Object o) {
        if (o instanceof Object[]) {
            Object[] ar = (Object[]) o;
            return Arrays.toString(ar);
        }
        if (o instanceof int[]) {
            int[] ar = (int[]) o;
            return Arrays.toString(ar);
        }
        if (o instanceof byte[]) {
            byte[] ar = (byte[]) o;
            return Arrays.toString(ar);
        }
        if (o instanceof short[]) {
            short[] ar = (short[]) o;
            return Arrays.toString(ar);
        }
        if (o instanceof long[]) {
            long[] ar = (long[]) o;
            return Arrays.toString(ar);
        }
        if (o instanceof float[]) {
            float[] ar = (float[]) o;
            return Arrays.toString(ar);
        }
        if (o instanceof double[]) {
            double[] ar = (double[]) o;
            return Arrays.toString(ar);
        }
        if (o instanceof char[]) {
            char[] ar = (char[]) o;
            return Arrays.toString(ar);
        }
        if (o instanceof boolean[]) {
            boolean[] ar = (boolean[]) o;
            return Arrays.toString(ar);
        }
        return o;
    }

    private static final boolean
            endsWith_array(String s, String[] possibilites) {
        for (String check : possibilites) {
            if (s.endsWith(check)) {
                return true;
            }
        }
        return false;
    }
}
