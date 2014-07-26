package com.techshroom.tscore.test;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.BeforeClass;
import org.junit.Test;

import com.techshroom.tscore.log.LogProvider;
import com.techshroom.tscore.math.exceptions.EvalException;
import com.techshroom.tscore.math.processor.ExpressionParseProvider;
import com.techshroom.tscore.math.processor.ExpressionProcessor;

public class ParsingTest {

    @BeforeClass
    public static void setupStreams() {
        LogProvider.init();
        LogProvider.overrideSTDStreams();
    }

    @SuppressWarnings("static-method")
    @Test
    public void onePlusOneIsTwo() {
        assertThat("1+1", BigDecimal.valueOf(2));
    }

    @SuppressWarnings("static-method")
    @Test
    public void onePlusNegOneIsZero() {
        assertThat("1+-1", BigDecimal.valueOf(0));
    }

    @SuppressWarnings("static-method")
    @Test
    public void sine90Is1() {
        assertThat("sin(90)", BigDecimal.valueOf(1));
    }

    @SuppressWarnings("static-method")
    @Test(expected = EvalException.class)
    public void malformedSineA() {
        assertThat("sin 90)", BigDecimal.valueOf(1));
    }

    @SuppressWarnings("static-method")
    @Test(expected = EvalException.class)
    public void malformedSineB() {
        assertThat("sin(90", BigDecimal.valueOf(1));
    }

    @SuppressWarnings("static-method")
    @Test(expected = EvalException.class)
    public void malformedSineC() {
        assertThat("sin 90", BigDecimal.valueOf(1));
    }

    private static void assertThat(String expr, BigDecimal eq) {
        System.err.println("Parsing '" + expr + "'; it should equal " + eq);
        ExpressionProcessor proc = ExpressionParseProvider.processorFor(expr);
        BigDecimal result = proc.process();
        System.err.println("" + expr + "=" + result);
        assertEquals(eq, result);
    }
}
