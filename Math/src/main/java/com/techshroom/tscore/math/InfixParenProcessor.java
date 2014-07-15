package com.techshroom.tscore.math;

import java.math.BigDecimal;
import java.util.LinkedList;

public final class InfixParenProcessor extends OperatorProcessor {
	private String store = "";
	private final LinkedList<String> stack = new LinkedList<String>();

	InfixParenProcessor(String proc) {
		super(proc);
	}

	@SuppressWarnings("boxing")
	@Override
	protected void preprocess() {
		for (char c : processing.toCharArray()) {
			if (Character.isDigit(c)) {
				pushItem(c);
			} else if (OperatorProcessor.isOperator(c)) {
				while (!stack.isEmpty()
						&& OperatorProcessor.isOperator(char_(stack.peek()))
						&& operatorCmp(c, char_(stack.peek())) < 0) {
					String op = stack.pop();
					pushItem(op);
				}
				stack.push(String.valueOf(c));
			} else if (c == '(') {
				stack.push(String.valueOf(c));
			} else if (c == ')') {
				String popped = stack.pop();
				while (char_(popped) != '(') {
					pushItem(popped);
					popped = stack.pop();
				}
			}
		}
		while (!stack.isEmpty()) {
			String popped = stack.pop();
			pushItem(popped);
		}
		store = store.substring(0, store.length() - 1);
	}

	private void pushItem(Object item) {
		store += item + " ";
	}

	private static int operatorCmp(char op_a, char op_b) {
		int[] table = priorityTable();
		return table[op_a] - table[op_b];
	}

	private static char char_(String s) {
		return s.charAt(0);
	}

	@Override
	public BigDecimal process() {
		return null;
	}

}
