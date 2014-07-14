package com.techshroom.tscore.math;

import java.math.BigDecimal;

public abstract class OperatorProcessor {
	protected final String processing;

	protected OperatorProcessor(String proc) {
		processing = proc;
		preprocess();
	}

	protected abstract void preprocess();

	public abstract BigDecimal process();
}
