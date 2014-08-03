package com.techshroom.tscore.math.operator;

import static com.techshroom.tscore.util.QuickStringBuilder.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

public final class Operator {
    public static final class OperatorLookupKey {
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

        /**
         * @return the placement
         */
        public NumberPlacement getPlacement() {
            return placement;
        }

        /**
         * @return the associativeness
         */
        public Associativeness getAssociativeness() {
            return assoc;
        }

        /**
         * @return the priority
         */
        public int getPriority() {
            return priority;
        }

        /**
         * @return the token
         */
        public String getToken() {
            return token;
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
                    / ((priority * token.hashCode()) + 1);
        }

        @SuppressWarnings("boxing")
        @Override
        public String toString() {
            return concatToStringComplex("OpKey<", "placement=", placement,
                    ",token=", token, ",associativeness=", assoc, ",priority=",
                    priority, ">");
        }
    }

    private static final Map<OperatorLookupKey, Operator> lookups = new HashMap<OperatorLookupKey, Operator>();
    private static final Map<String, Operator> tokenLookups = new HashMap<String, Operator>();

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
            } else if (olk.placement != target.placement) {
                sb.append("the ")
                        .append("placement mismatched (should have been ")
                        .append(olk.placement).append(" was ")
                        .append(target.placement).append(")");
            } else {
                sb.append("an unknown error occured");
            }
            throw new IllegalArgumentException(sb.toString());
        }

        // no operator yet: define it
        o = new Operator(target, runner);
        lookups.put(target, o);
        tokenLookups.put(token, o);
        return getOperator(token);
    }

    public static Operator getOperator(String token) {
        return tokenLookups.get(token);
    }

    public static Operator getOperator(String token,
            NumberPlacement requestedPlacement) {
        Operator o = getOperator(token);
        if (o == null || o.ourKey.placement != requestedPlacement) {
            return null;
        }
        return o;
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
            throw new UnsupportedOperationException(
                    "Unknown Number extender " + a.getClass());
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
            System.err
                    .println("[WARNING] Unaccounted for Number extender " + ac);
        }
        return tk.cast(ret);
    }

    private static boolean isLong(Class<? extends Number> a) {
        return a == Integer.class || a == Long.class
                || a == Byte.class
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

    public OperatorLookupKey getKey() {
        return ourKey;
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
                        + " with placement "
                        + ourKey.placement
                        + " (we are "
                        + toString()
                        + ")");
                break;
        }
        return builder.toString();
    }

    private void checkInputs(Number[] in) {
        if (in.length < inputCount()) {
            throw new IllegalArgumentException(
                    "Not enough numbers for input " + concatToString(
                            "(needed ", Integer.valueOf(inputCount()),
                            ", got ", Integer.valueOf(in.length), ")"));
        }
    }

    private static void defaults() {
        registerOrGetOperator("+", 0, Associativeness.LEFT,
                NumberPlacement.BOTH, new OperatorRunner() {

                    @Override
                    public long runLong(long... longs) {
                        return longs[0] + longs[1];
                    }

                    @Override
                    public double runDouble(double... doubles) {
                        return doubles[0] + doubles[1];
                    }

                    @Override
                    public BigDecimal runBigDec(BigDecimal... bigdecimals) {
                        return bigdecimals[0].add(bigdecimals[1]);
                    }
                });
        registerOrGetOperator("+", 0, Associativeness.LEFT,
                NumberPlacement.RIGHT, new OperatorRunner() {

                    @Override
                    public long runLong(long... longs) {
                        return +longs[0];
                    }

                    @Override
                    public double runDouble(double... doubles) {
                        return +doubles[0];
                    }

                    @Override
                    public BigDecimal runBigDec(BigDecimal... bigdecimals) {
                        return bigdecimals[0].plus();
                    }
                });
        registerOrGetOperator("-", 0, Associativeness.LEFT,
                NumberPlacement.RIGHT, new OperatorRunner() {

                    @Override
                    public long runLong(long... longs) {
                        return -longs[0];
                    }

                    @Override
                    public double runDouble(double... doubles) {
                        return -doubles[0];
                    }

                    @Override
                    public BigDecimal runBigDec(BigDecimal... bigdecimals) {
                        return bigdecimals[0].negate();
                    }
                });
    }

    static {
        defaults();
    }
}
