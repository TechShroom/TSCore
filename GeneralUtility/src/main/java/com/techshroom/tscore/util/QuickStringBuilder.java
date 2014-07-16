package com.techshroom.tscore.util;

public final class QuickStringBuilder {
	private QuickStringBuilder() {
	}

	/**
	 * Makes a string from the result of concatenating the given argumnts.
	 * 
	 * @param args - the input
	 * @return the resultant string
	 */
	public static final String build(Object... args) {
		StringBuilder sb = new StringBuilder();
		for (Object o : args) {
			sb.append(o);
		}
		return sb.toString();
	}
}
