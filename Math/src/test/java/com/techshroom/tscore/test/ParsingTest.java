package com.techshroom.tscore.test;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.*;

import com.techshroom.tscore.log.LogProvider;
import com.techshroom.tscore.math.exceptions.EvalException;
import com.techshroom.tscore.math.processor.*;
import com.techshroom.tscore.math.processor.token.DeferredInfixToken;

@Ignore
@SuppressWarnings("static-method")
public class ParsingTest {

    @BeforeClass
    public static void setupStreams() {
        LogProvider.init();
        LogProvider.overrideSTDStreams();
    }

    @Test
    public void onePlusOneIsTwo() throws Throwable {
        assertThat("1+1", BigDecimal.valueOf(2));
    }

    @Test
    public void onePlusNegOneIsZero() throws Throwable {
        assertThat("1+-1", BigDecimal.valueOf(0));
    }

    @Test
    public void sine90Is1() throws Throwable {
        assertThat("sin(90)", BigDecimal.valueOf(1));
    }

    @Test(expected = EvalException.class)
    public void malformedSineA() throws Throwable {
        assertThatWillFail("sin 90)");
    }

    @Test(expected = EvalException.class)
    public void malformedSineB() throws Throwable {
        assertThatWillFail("sin(90");
    }

    @Test(expected = EvalException.class)
    public void malformedSineC() throws Throwable {
        assertThatWillFail("sin 90");
    }

    @Test(expected = EvalException.class)
    public void malformedOps() throws Throwable {
        assertThatWillFail("[90|");
    }

    @Test(expected = EvalException.class)
    public void emptyParens() throws Throwable {
        assertThatWillFail("()");
    }

    @Test(expected = EvalException.class)
    public void onlyLeftParen() throws Throwable {
        assertThatWillFail("(90");
    }

    @Test(expected = EvalException.class)
    public void onlyRightParen() throws Throwable {
        assertThatWillFail("90)");
    }

    @Test(expected = EvalException.class)
    public void backwardsParens() throws Throwable {
        assertThatWillFail(")90(");
    }

    private static void assertThatWillFail(String expr) throws Throwable {
        System.err.print(expr + " should be invalid; ");
        assertThat(expr, null);
    }

    private static void assertThat(String expr, BigDecimal eq) throws Throwable {
        Throwable toThrow = null;
        ExpressionProcessor ep = ExpressionParseProvider.processorFor(expr);
        try {
            assertThat(expr, eq, ep);
        } catch (Throwable t) {
            toThrow = t;
        }
        try {
            if (ep instanceof InfixProcessor) {
                assertThat(
                        expr,
                        eq,
                        new DeferredInfixToken(ExpressionProcessor
                                .generateTokens(expr, null)));
            } else {
                fail("Unknown processor " + ep.getClass() + ".");
            }
        } catch (AssertionError reThrow) {
            throw reThrow;
        } catch (Throwable t) {
            if (toThrow != null) {
                System.err.println("Disregarding second throwable: " + t
                        .getLocalizedMessage());
            } else {
                toThrow = t;
            }
        }
        if (toThrow != null) {
            throw toThrow;
        }
    }

    private static void assertThat(String expr, BigDecimal eq,
            ExpressionProcessor proc) {
        System.err.println("Parsing '" + expr
                + "' with "
                + proc
                + "; it should equal "
                + eq);
        BigDecimal result = proc.process();
        System.err.println("" + expr + "=" + result);
        assertEquals(eq, result);
    }
}
