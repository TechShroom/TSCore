package com.techshroom.tscore.math;

import java.math.BigDecimal;
import java.util.LinkedList;

public final class InfixParenProcessor extends OperatorProcessor {
	private final LinkedList<String> stack = new LinkedList<String>();

	InfixParenProcessor(String proc) {
		super(proc);
	}

	@Override
	protected void preprocess() {

	}

	@Override
	public BigDecimal process() {
		return null;
	}

}
