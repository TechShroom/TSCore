package com.techshroom.tscore.math;

import java.math.BigDecimal;
import java.util.LinkedList;

public final class InfixNoParenProcessor extends OperatorProcessor {
	private final LinkedList<String> numbers = new LinkedList<String>();
	private final LinkedList<String> operators = new LinkedList<String>();

	InfixNoParenProcessor(String proc) {
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
