package com.techshroom.tscore.math;

import java.math.BigInteger;

public final class BIMath {

    private BIMath() {
    }

    public static boolean lessThan(BigInteger a, BigInteger b) {
        return a.compareTo(b) < 0;
    }

    public static boolean lessThanEquals(BigInteger a, BigInteger b) {
        return a.compareTo(b) <= 0;
    }

    public static boolean greaterThan(BigInteger a, BigInteger b) {
        return a.compareTo(b) > 0;
    }

    public static boolean greaterThanEquals(BigInteger a, BigInteger b) {
        return a.compareTo(b) >= 0;
    }

    public static boolean equals(BigInteger a, BigInteger b) {
        return a.compareTo(b) == 0;
    }
}
