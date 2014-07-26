package com.techshroom.tscore.math.operator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import com.techshroom.tscore.util.QuickStringBuilder;

public final class Operator {
    private static final class OperatorLookupKey {
        private final NumberPlacement placement;
        private final Associativeness assoc;
        private final int priority;
        private final String token;

        private OperatorLookupKey(NumberPlacement plcmnt, Associativeness assc,
                int pri, String tkn) {
            placement = plcmnt;
            assoc = assc;
            priority = pri;
            token = tkn;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof OperatorLookupKey) {
                OperatorLookupKey olk = (OperatorLookupKey) obj;
                return olk.placement == placement && olk.priority == priority
                        && olk.assoc == assoc
                        && token.equalsIgnoreCase(olk.token);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return placement.hashCode() + assoc.hashCode()
                    / (priority / token.hashCode());
        }

        @SuppressWarnings("boxing")
        @Override
        public String toString() {
            return QuickStringBuilder.build("OpKey<", "placement=", placement,
                    ",token=", token, ",associativeness=", assoc, ",priority=",
                    priority, ">");
        }
    }

    private static final class TknAndPlaceKey {
        private final NumberPlacement placement;
        private final String token;

        private TknAndPlaceKey(NumberPlacement plcmnt, String tkn) {
            placement = plcmnt;
            token = tkn;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof OperatorLookupKey) {
                OperatorLookupKey olk = (OperatorLookupKey) obj;
                return olk.placement == placement
                        && token.equalsIgnoreCase(olk.token);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return placement.hashCode() + token.hashCode();
        }

        @Override
        public String toString() {
            return QuickStringBuilder.build("OpKey2<", "placement=", placement,
                    ",token=", token, ">");
        }
    }

    private static final Map<OperatorLookupKey, Operator> lookups = new HashMap<OperatorLookupKey, Operator>();
    private static final Map<TknAndPlaceKey, Operator> tokenLookups = new HashMap<TknAndPlaceKey, Operator>();

    public static final Comparator<Operator> ASSOC_COMPARATOR = new Comparator<Operator>() {
        @Override
        public int compare(Operator o1, Operator o2) {
            return o1.ourKey.assoc.compareTo(o2.ourKey.assoc);
        }
    };
    public static final Comparator<Operator> PRIORITY_COMPARATOR = new Comparator<Operator>() {
        @Override
        public int compare(Operator o1, Operator o2) {
            return o1.ourKey.priority - o2.ourKey.priority;
        }
    };
    public static final Comparator<Operator> TOKEN_COMPARATOR = new Comparator<Operator>() {
        @Override
        public int compare(Operator o1, Operator o2) {
            return o1.ourKey.token.compareTo(o2.ourKey.token);
        }
    };

    public static Operator registerOrGetOperator(String token, int prio,
            Associativeness assoc, NumberPlacement placement,
            OperatorRunner runner) {
        OperatorLookupKey target = new OperatorLookupKey(placement, assoc,
                prio, token);
        TknAndPlaceKey otherTarget = new TknAndPlaceKey(placement, token);
        Operator o = getOperator(token, placement);
        if (o != null) {
            OperatorLookupKey olk = o.ourKey;
            if (olk.equals(target)) {
                return o;
            }
            // something mismatched...but the token matches. What do?
            StringBuilder sb = new StringBuilder(
                    "Cannot override the operator ").append(token).append(
                    " because ");
            if (olk.priority != target.priority) {
                sb.append("the ")
                        .append("priority mismatched (should have been ")
                        .append(olk.priority).append(" was ")
                        .append(target.priority).append(")");
            } else if (olk.assoc != target.assoc) {
                sb.append("the ")
                        .append("associativeness mismatched (should have been ")
                        .append(olk.assoc).append(" was ").append(target.assoc)
                        .append(")");
            } else {
                // NB: placement isn't handled because it is different.
                sb.append("an unknown error occured");
            }
            throw new IllegalArgumentException(sb.toString());
        }

        // no operator yet: define it
        o = new Operator(target, runner);
        lookups.put(target, o);
        tokenLookups.put(otherTarget, o);
        return getOperator(token, placement);
    }

    public static Operator getOperator(String token,
            NumberPlacement requestedPlacement) {
        return tokenLookups.get(new TknAndPlaceKey(requestedPlacement, token));
    }
    
    public static Operator[] operatersForToken(String token) {
        Operator[] ops = new Operator[NumberPlacement.values().length];
        NumberPlacement[] allPlacements = NumberPlacement.values();
        for (int i = 0; i < allPlacements.length; i++) {
            NumberPlacement np = allPlacements[i];
            ops[i] = getOperator(token, np);
        }
        return ops;
    }

    private final OperatorLookupKey ourKey;

    private final OperatorRunner runner;

    private Operator(OperatorLookupKey key, OperatorRunner or) {
        ourKey = key;
        runner = or;
    }

    @SuppressWarnings("boxing")
    public <T extends Number> T runTheNumbers(T... numbers) {
        checkInputs(numbers);
        // always at least one
        T a = numbers[0];
        Number theAnswer = null;
        if (isLong(a.getClass())) {
            theAnswer = runner.runLong(toLongs(numbers));
        } else if (isDouble(a.getClass())) {
            theAnswer = runner.runDouble(toDoubles(numbers));
        } else if (isBigDec(a.getClass())) {
            theAnswer = runner.runBigDec(toBigDec(numbers));
        } else {
            throw new UnsupportedOperationException("Unknown Number extender "
                    + a.getClass());
        }

        Class<? extends Number> ac = a.getClass(), tc = theAnswer.getClass();

        Number ret = null;
        @SuppressWarnings("unchecked")
        Class<T> tk = (Class<T>) numbers.getClass().getComponentType();

        if (ac == tc) {
            ret = theAnswer;
        } else if (ac == Byte.class) {
            ret = Byte.valueOf(theAnswer.byteValue());
        } else if (ac == Short.class) {
            ret = Short.valueOf(theAnswer.shortValue());
        } else if (ac == Integer.class) {
            ret = Integer.valueOf(theAnswer.intValue());
        } else if (ac == Long.class) {
            ret = Long.valueOf(theAnswer.longValue());
        } else if (ac == Double.class) {
            ret = Double.valueOf(theAnswer.doubleValue());
        } else if (ac == Float.class) {
            ret = Float.valueOf(theAnswer.floatValue());
        } else if (ac == BigDecimal.class) {
            if (tc == BigInteger.class) {
                // avoid loss of bigint
                ret = new BigDecimal((BigInteger) theAnswer);
            } else {
                ret = BigDecimal.valueOf(theAnswer.doubleValue());
            }
        } else if (ac == BigInteger.class) {
            if (tc == BigDecimal.class) {
                // avoid loss of bigdec
                ret = ((BigDecimal) theAnswer).toBigInteger();
            } else {
                ret = BigInteger.valueOf(theAnswer.longValue());
            }
        } else {
            System.err.println("[WARNING] Unaccounted for Number extender "
                    + ac);
        }
        return tk.cast(ret);
    }

    private static boolean isLong(Class<? extends Number> a) {
        return a == Integer.class || a == Long.class || a == Byte.class
                || a == Short.class;
    }

    private static boolean isDouble(Class<? extends Number> a) {
        return a == Double.class || a == Float.class;
    }

    private static boolean isBigDec(Class<? extends Number> a) {
        return a == BigDecimal.class || a == BigInteger.class;
    }

    private static long[] toLongs(Number... numbers) {
        long[] res = new long[numbers.length];
        for (int i = 0; i < numbers.length; i++) {
            res[i] = numbers[i].longValue();
        }
        return res;
    }

    private static double[] toDoubles(Number... numbers) {
        double[] res = new double[numbers.length];
        for (int i = 0; i < numbers.length; i++) {
            res[i] = numbers[i].doubleValue();
        }
        return res;
    }

    private static BigDecimal[] toBigDec(Number... numbers) {
        BigDecimal[] res = new BigDecimal[numbers.length];
        for (int i = 0; i < numbers.length; i++) {
            Number n = numbers[i];
            if (n instanceof BigDecimal) {
                res[i] = (BigDecimal) n;
            } else if (n instanceof BigInteger) {
                res[i] = new BigDecimal((BigInteger) n);
            } else {
                res[i] = BigDecimal.valueOf(n.doubleValue());
            }
        }
        return res;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof Operator) {
            Operator o = (Operator) obj;
            return ourKey.equals(o.ourKey);
        }

        return false;
    }

    public int inputCount() {
        return (ourKey.placement == NumberPlacement.BOTH) ? 2 : 1;
    }

    @Override
    public int hashCode() {
        return ourKey.hashCode();
    }

    @Override
    public String toString() {
        return ourKey.token;
    }

    public String toString(Number... numbers) {
        checkInputs(numbers);
        StringBuilder builder = new StringBuilder();
        switch (ourKey.placement) {
            case LEFT:
                builder.append(numbers[0]);
                builder.append(ourKey.token);
                break;
            case RIGHT:
                builder.append(ourKey.token);
                builder.append(numbers[0]);
                break;
            case BOTH:
                builder.append(numbers[0]);
                builder.append(ourKey.token);
                builder.append(numbers[1]);
                break;
            default:
                builder.append("Failed on " + Arrays.toString(numbers)
                        + " with placement " + ourKey.placement + " (we are "
                        + toString() + ")");
                break;
        }
        return builder.toString();
    }

    @SuppressWarnings("boxing")
    private void checkInputs(Number[] in) {
        if (in.length < inputCount()) {
            throw new IllegalArgumentException("Not enough numbers for input "
                    + QuickStringBuilder.build("(needed ", inputCount(),
                            ", got ", in.length, ")"));
        }
    }
}
