package com.techshroom.tscore.math;

import java.math.BigDecimal;

public abstract class OperatorProcessor {
	private static final int[] PRIORITY_TABLE;

	static {
		int[] table = new int[256];
		for (int i = 0; i < table.length; i++) {
			table[i] = Integer.MIN_VALUE;
		}
		int prio = Integer.MAX_VALUE;
		table['^'] = prio--;
		table['*'] = table['/'] = prio--;
		table['+'] = table['-'] = prio--;
		PRIORITY_TABLE = table;
	}

	protected static boolean isOperator(char c) {
		return c == '+' || c == '-' || c == '*' || c == '/' || c == '%';
	}

	protected static int[] priorityTable() {
		return PRIORITY_TABLE;
	}

	protected final String processing;

	protected OperatorProcessor(String proc) {
		processing = proc;
		preprocess();
	}

	protected abstract void preprocess();

	public abstract BigDecimal process();
}
