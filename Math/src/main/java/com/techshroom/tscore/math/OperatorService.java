package com.techshroom.tscore.math;

import java.util.ArrayList;
import java.util.List;

import com.techshroom.tscore.util.Getter;
import com.techshroom.tscore.util.GetterKey;
import com.techshroom.tscore.util.MatchCheckAndGet;
import com.techshroom.tscore.util.MatchChecker;
import com.techshroom.tscore.util.SimpleMatchCheckAndGet;

public final class OperatorService {
	public static final GetterKey<String> EXPRESSION_KEY = new GetterKey<String>(
			"expr");
	private static final List<MatchCheckAndGet<String, ? extends OperatorProcessor>> factories = new ArrayList<MatchCheckAndGet<String, ? extends OperatorProcessor>>();

	private OperatorService() {
	}

	public static OperatorProcessor processorFor(String expression) {
		for (MatchCheckAndGet<String, ? extends OperatorProcessor> fact : factories) {
			if (fact.matches(expression)) {
				fact.setData(EXPRESSION_KEY, expression);
				return fact.get();
			}
		}
		return null;
	}

	public static <T extends OperatorProcessor> void registerOperator(
			MatchCheckAndGet<String, T> factory) {
		factories.add(factory);
	}

	static {
		// our procs
		registerOperator(new SimpleMatchCheckAndGet<String, InfixParenProcessor>(
				new MatchChecker<String>() {
					@Override
					public boolean matches(String t) {
						return t.indexOf('(') >= 0;
					}
				}, new Getter<InfixParenProcessor>() {
					private String expr;

					@Override
					public InfixParenProcessor get() {
						if (expr == null) {
							throw new IllegalStateException(
									"must set the expression");
						}
						return new InfixParenProcessor(expr);
					}

					@Override
					public <X> void setData(GetterKey<X> key, X value) {
						if (key == EXPRESSION_KEY) {
							expr = EXPRESSION_KEY.cast(value);
						}
					}
				}));
		registerOperator(new SimpleMatchCheckAndGet<String, InfixNoParenProcessor>(
				new MatchChecker<String>() {
					@Override
					public boolean matches(String t) {
						return t.indexOf('(') < 0;
					}
				}, new Getter<InfixNoParenProcessor>() {
					private String expr;

					@Override
					public InfixNoParenProcessor get() {
						if (expr == null) {
							throw new IllegalStateException(
									"must set the expression");
						}
						return new InfixNoParenProcessor(expr);
					}

					@Override
					public <X> void setData(GetterKey<X> key, X value) {
						if (key == EXPRESSION_KEY) {
							expr = EXPRESSION_KEY.cast(value);
						}
					}
				}));
	}
}
