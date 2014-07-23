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
			if (o instanceof String) {
				sb.append("'");
			}
			sb.append(o);
			if (o instanceof String) {
				sb.append("'");
			}
		}
		return sb.toString();
	}
}
