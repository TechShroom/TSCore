package com.techshroom.tscore.math.processor;

import java.util.ArrayList;
import java.util.List;

import com.techshroom.tscore.util.GetterKey;
import com.techshroom.tscore.util.MatchCheckAndGet;
import com.techshroom.tscore.util.MatchChecker;
import com.techshroom.tscore.util.SimpleGetter;
import com.techshroom.tscore.util.SimpleMatchCheckAndGet;

public final class ExpressionParseProvider {
	public static final GetterKey<String> EXPRESSION_KEY = new GetterKey<String>(
			"expr");
	private static final List<MatchCheckAndGet<String, ? extends ExpressionProcessor>> factories = new ArrayList<MatchCheckAndGet<String, ? extends ExpressionProcessor>>();

	private ExpressionParseProvider() {
	}

	public static ExpressionProcessor processorFor(String expression) {
		for (MatchCheckAndGet<String, ? extends ExpressionProcessor> fact : factories) {
			if (fact.matches(expression)) {
				fact.setData(EXPRESSION_KEY, expression);
				return fact.get();
			}
		}
		return null;
	}

	public static <T extends ExpressionProcessor> void registerProcessor(
			MatchCheckAndGet<String, T> factory) {
		factories.add(factory);
	}

	static {
		// our procs
		registerProcessor(new SimpleMatchCheckAndGet<String, InfixProcessor>(
				new MatchChecker<String>() {
					@Override
					public boolean matches(String t) {
						return /* TODO: detect diff btwn post/in fix */true;
					}
				}, new SimpleGetter<InfixProcessor, String>() {
					@Override
					protected InfixProcessor doGet() {
						return new InfixProcessor(getARG());
					}

					@Override
					public <X> void setData(GetterKey<X> key, X value) {
						if (key == EXPRESSION_KEY) {
							super.setData(key(), EXPRESSION_KEY.cast(value));
						}
					}
				}));
		registerProcessor(new SimpleMatchCheckAndGet<String, PostfixProcessor>(
				new MatchChecker<String>() {
					@Override
					public boolean matches(String t) {
						return /* TODO: detect diff btwn post/in fix */false;
					}
				}, new SimpleGetter<PostfixProcessor, String>() {
					@Override
					protected PostfixProcessor doGet() {
						return new PostfixProcessor(getARG());
					}

					@Override
					public <X> void setData(GetterKey<X> key, X value) {
						if (key == EXPRESSION_KEY) {
							super.setData(key(), EXPRESSION_KEY.cast(value));
						}
					}
				}));
	}
}
