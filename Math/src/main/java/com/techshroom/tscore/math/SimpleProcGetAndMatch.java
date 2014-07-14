package com.techshroom.tscore.math;

import com.techshroom.tscore.util.MatchCheckAndGet;
import com.techshroom.tscore.util.MatchChecker;

public class SimpleProcGetAndMatch<T extends OperatorProcessor> implements
		MatchCheckAndGet<String, T> {
	private final MatchChecker<String> matcher;

	public SimpleProcGetAndMatch(MatchChecker<String> matchr) {
		matcher = matchr;
	}

	@Override
	public boolean matches(String expr) {
		return matcher.matches(expr);
	}

	@Override
	public T getIfMatches(String expr) {
		return matches(expr) ? get() : null;
	}

	@Override
	public T get() {
		return null;
	}

}
