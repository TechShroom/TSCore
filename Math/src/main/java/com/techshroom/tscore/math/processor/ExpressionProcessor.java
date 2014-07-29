package com.techshroom.tscore.math.processor;

import static com.techshroom.tscore.math.processor.RegexPatterns.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import com.techshroom.tscore.math.exceptions.EvalException;
import com.techshroom.tscore.math.exceptions.EvalException.Reason;
import com.techshroom.tscore.math.processor.token.*;
import com.techshroom.tscore.regex.ExtendedMatcher;
import com.techshroom.tscore.regex.MatchInfo;

public abstract class ExpressionProcessor {
    public static final List<Token> generateTokens(String processing,
            ExpressionProcessor callback) {
        System.err.println("Generating for '" + processing + "'");
        List<Token> building = new ArrayList<Token>();
        int nextTokenIndex = 0;
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
            Token token;
            n = extN.lookingAt(nextTokenIndex);
            o = extO.lookingAt(nextTokenIndex);
            f = extF.lookingAt(nextTokenIndex);
            s = extS.lookingAt(nextTokenIndex);
            if (s.hasMatch()) {
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
            } else if (processing.charAt(nextTokenIndex) == '(') {
                token = (new BasicToken("(", TokenFlag.OTHER));
                nextTokenIndex++;
            } else if (processing.charAt(nextTokenIndex) == ')') {
                token = (new BasicToken(")", TokenFlag.OTHER));
                nextTokenIndex++;
            } else if (processing.charAt(nextTokenIndex) == ' ') {
                nextTokenIndex++;
                continue;
            } else {
                // we don't know how to handle this
                throw new EvalException(Reason.UKNOWN_ERROR, "Cannot process "
                        + processing.charAt(nextTokenIndex));
            }
            System.err.println(token);
            if (callback != null) {
                callback.onToken(token, nextTokenIndex);
            }
            building.add(token);
        }
        return building;
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
