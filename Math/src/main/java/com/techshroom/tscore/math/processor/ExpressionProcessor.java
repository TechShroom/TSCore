package com.techshroom.tscore.math.processor;

import static com.techshroom.tscore.math.processor.RegexBits.*;
import static com.techshroom.tscore.math.processor.RegexPatterns.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;

import com.techshroom.tscore.math.exceptions.EvalException;
import com.techshroom.tscore.math.exceptions.EvalException.Reason;
import com.techshroom.tscore.math.operator.Operator;
import com.techshroom.tscore.math.processor.token.*;
import com.techshroom.tscore.regex.ExtendedMatcher;
import com.techshroom.tscore.regex.MatchInfo;

public abstract class ExpressionProcessor {
    public static final List<Token> generateTokens(String processing,
            ExpressionProcessor callback) {
        System.err.println("Generating for '" + processing + "'");
        List<Token> building = new ArrayList<Token>();
        int nextTokenIndex = 0, hold = nextTokenIndex;
        // we can tokenize:
        // operators: required to tokenize: at least one @ var, at most two,
        // make it a function after that
        // functions can tokenize with something like f(@x) for f(x)
        // numbers are ([-+]?[0-9]+(\.[0-9]+)?([eE][-+]?[0-9]+)?)
        // catches all things
        Matcher numMatcher = NUMBER_REGEX.matcher(processing);
        Matcher funcMatcher = FUNCTION_GET_REGEX.matcher(processing);
        Matcher opMatcher = OPERATOR_GET_REGEX.matcher(processing);
        Matcher stringMatcher = STRING_GET_REGEX.matcher(processing);

        ExtendedMatcher extN = ExtendedMatcher.get(numMatcher);
        ExtendedMatcher extO = ExtendedMatcher.get(opMatcher);
        ExtendedMatcher extF = ExtendedMatcher.get(funcMatcher);
        ExtendedMatcher extS = ExtendedMatcher.get(stringMatcher);
        // int next = 0;
        MatchInfo n, o, f, s;

        while (nextTokenIndex < processing.length()) {
            hold = nextTokenIndex;
            Token token;
            n = extN.lookingAt(hold);
            o = extO.lookingAt(hold);
            f = extF.lookingAt(hold);
            s = extS.lookingAt(hold);
            if (processing.charAt(hold) == ' ') {
                nextTokenIndex++;
                continue;
            } else if (s.hasMatch()) {
                token = (new BasicToken(s.getMatch(), TokenFlag.STRING));
                nextTokenIndex = s.getEnd();
            } else if (o.hasMatch() && !o.getMatch().matches("\\(|\\)")) {
                token = (new BasicToken(o.getMatch(), TokenFlag.OPERATOR));
                nextTokenIndex = o.getEnd();
            } else if (n.hasMatch()) {
                token = (new NumberToken(new BigDecimal(n.getMatch())));
                nextTokenIndex = n.getEnd();
            } else if (f.hasMatch()) {
                token = (new BasicToken(f.getMatch(), TokenFlag.FUNCTION));
                nextTokenIndex = f.getEnd();
            } else if (processing.charAt(hold) == '(') {
                token = (new BasicToken("(", TokenFlag.OTHER));
                nextTokenIndex++;
            } else if (processing.charAt(hold) == ')') {
                token = (new BasicToken(")", TokenFlag.OTHER));
                nextTokenIndex++;
            } else {
                // we don't know how to handle this
                throw new EvalException(Reason.UKNOWN_ERROR,
                        "Cannot process " + processing.charAt(hold));
            }
            if (token instanceof BasicToken) {
                BasicToken bt = (BasicToken) token;
                if (bt.flag() == TokenFlag.OPERATOR) {
                    String test = bt.value();
                    String orig = test;
                    Deque<MatchInfo> check = new ArrayDeque<MatchInfo>(
                            test.length());
                    Operator flag = Operator.getOperator(test);
                    boolean flagPassed = false;
                    while (!flagPassed) {
                        splitInto(test, check, orig);
                        while (!check.isEmpty()) {
                            MatchInfo mtest = check.pollLast();
                            test = mtest.getMatch();
                            flag = Operator.getOperator(test);
                            if (flag != null) {
                                static_onToken(new BasicToken(test, bt.flag()),
                                        callback, hold + orig.indexOf(test),
                                        building);
                                flag = null;
                                flagPassed |= true;
                                continue;
                            }
                            if (test.length() == 1 || (!splitInto(test, check,
                                    orig) && flagPassed)) {
                                throw new EvalException(
                                        Reason.NO_SUCH_OPERATOR,
                                        orig.substring(mtest.getStart(),
                                                mtest.getEnd()));
                            }
                        }
                    }
                    continue;
                } else if (bt.flag() == TokenFlag.FUNCTION) {
                    String funcWithArgs = bt.value();
                    String name = funcWithArgs.replaceFirst(FUNCTION + LPAREN
                            + "(.+)"
                            + RPAREN, "$1");

                    // name
                    static_onToken(new BasicToken(name, bt.flag()), callback,
                            hold, building);

                    // (
                    static_onToken(new BasicToken("(", TokenFlag.OTHER),
                            callback, hold + name.length(), building);
                    // @a, @b, @c, ...
                    static_onToken(
                            createArgsList(funcWithArgs, hold + name.length()
                                    + 1), callback, hold + name.length() + 1,
                            building);

                    // )
                    static_onToken(new BasicToken(")", TokenFlag.OTHER),
                            callback, hold + funcWithArgs.length() - 1,
                            building);
                    continue;
                }
            }
            static_onToken(token, callback, hold, building);
        }
        return building;
    }

    private static final Integer BRCKT = Integer.valueOf(1), PAREN = Integer
            .valueOf(2), NONE = null;

    private static Token createArgsList(String funcWithArgs, int indBase) {
        List<Token> args = new ArrayList<Token>();
        String argsStr = funcWithArgs.replaceFirst(FUNCTION + LPAREN
                + "(.+)"
                + RPAREN, "$2");
        char[] str_to_chr = argsStr.toCharArray();
        String parseNext = "";
        LinkedList<Integer> modes = new LinkedList<Integer>();
        for (int i = 0; i < str_to_chr.length; i++) {
            char x = str_to_chr[i];
            if (x == '[') {
                // end of [...]
                modes.push(BRCKT);
            } else if (x == '(') {
                // start of (...)
                modes.push(PAREN);
            } else if (x == ']') {
                // end of [...]
                Integer peek = modes.peek();
                if (peek != BRCKT) {
                    throw new EvalException(Reason.PARSE_ERROR,
                            peek == PAREN ? ")"
                                    : peek == NONE ? "probably a comma" : peek,
                            Integer.valueOf(indBase + i));
                }
            } else if (x == ')') {
                // end of (...)
                Integer peek = modes.peek();
                if (peek != PAREN) {
                    throw new EvalException(Reason.PARSE_ERROR,
                            peek == BRCKT ? "]"
                                    : peek == NONE ? "probably a comma" : peek,
                            Integer.valueOf(indBase + i));
                }
            } else if (x == ',' || (i + 1) == str_to_chr.length) {
                // end of argument
                if (modes.isEmpty()) {
                    // not in list/function
                    args.add(new DoubleDeferredInfixToken(parseNext));
                }
            } else {
                parseNext += x;
            }
        }
        return new ListToken(args);
    }

    private static boolean splitInto(String test, Deque<MatchInfo> check,
            String orig) {
        int len = test.length();
        int lenO2 = len / 2;
        int szBfore = check.size();
        if (lenO2 != 0) {
            final int lenO2f = lenO2;
            final String out = test.substring(0, lenO2);
            check.push(MatchInfo.saveState(new MatchResult() {

                @Override
                public int start(int group) {
                    return start();
                }

                @Override
                public int start() {
                    return 0;
                }

                @Override
                public int groupCount() {
                    return 1;
                }

                @Override
                public String group(int group) {
                    return group();
                }

                @Override
                public String group() {
                    return out;
                }

                @Override
                public int end(int group) {
                    return end();
                }

                @Override
                public int end() {
                    return lenO2f;
                }
            }, true));
        }
        if (len != lenO2) {
            final int lenO2f = lenO2;
            final int lenf = len;
            final String out = test.substring(lenO2, len);
            check.push(MatchInfo.saveState(new MatchResult() {

                @Override
                public int start(int group) {
                    return start();
                }

                @Override
                public int start() {
                    return lenO2f;
                }

                @Override
                public int groupCount() {
                    return 1;
                }

                @Override
                public String group(int group) {
                    return group();
                }

                @Override
                public String group() {
                    return out;
                }

                @Override
                public int end(int group) {
                    return end();
                }

                @Override
                public int end() {
                    return lenf;
                }
            }, true));
        }
        return check.size() != szBfore;
    }

    private static void static_onToken(Token token,
            ExpressionProcessor callback, int nextTokenIndex,
            List<Token> building) {
        System.err.println(token);
        if (callback != null) {
            callback.onToken(token, nextTokenIndex);
        }
        building.add(token);
    }

    protected final String processing;

    protected ExpressionProcessor(String proc) throws EvalException {
        processing = proc.trim();
        // tokenize();
    }

    protected final void tokenize() throws EvalException {
        generateTokens(processing, this);
    }

    /**
     * A reminder you need to call tokenize.
     */
    protected abstract void callTokenize();

    protected abstract void onToken(Token token, int index);

    public abstract BigDecimal process();
}
