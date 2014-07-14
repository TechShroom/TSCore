package com.techshroom.tscore.util;

public class SimpleMatchCheckAndGet<INPUT, OUTPUT> implements
		MatchCheckAndGet<INPUT, OUTPUT> {
	private final MatchChecker<INPUT> matcher;
	private final Getter<OUTPUT> getter;

	public SimpleMatchCheckAndGet(MatchChecker<INPUT> matchr,
			Getter<OUTPUT> gttr) {
		matcher = matchr;
		getter = gttr;
	}

	@Override
	public boolean matches(INPUT expr) {
		return matcher.matches(expr);
	}

	@Override
	public OUTPUT getIfMatches(INPUT expr) {
		return matches(expr) ? get() : null;
	}

	@Override
	public OUTPUT get() {
		return getter.get();
	}

}
