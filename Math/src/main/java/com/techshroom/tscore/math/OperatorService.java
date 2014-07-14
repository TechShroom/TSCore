package com.techshroom.tscore.math;

import com.techshroom.tscore.util.MatchCheckAndGet;

public final class OperatorService {
	private OperatorService() {
	}

	public static OperatorProcessor processorFor(String expression) {
		// if (expression)
		return null;
	}

	public static <T extends OperatorProcessor> void registerOperator(
			MatchCheckAndGet<String, T> factory) {

	}
}
