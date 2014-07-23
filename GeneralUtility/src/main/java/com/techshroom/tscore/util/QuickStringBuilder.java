package com.techshroom.tscore.util;

public final class QuickStringBuilder {
	private static final String[] BUILD_DEFAULT_ENDSWITH = "=|->|:".split("\\|");

	private QuickStringBuilder() {
	}

	/**
	 * Makes a string from the result of concatenating the given argumnts.
	 * 
	 * @param args
	 *            - the input
	 * @return the resultant string
	 */
	public static final String build(Object... args) {
		StringBuilder sb = new StringBuilder();
		boolean addQuotes = false;
		for (Object o : args) {
			if (o instanceof String && addQuotes) {
				sb.append("'");
			}
			sb.append(o);
			if (o instanceof String) {
				if (addQuotes) {
					sb.append("'");
				} else {
					addQuotes = endsWith_array((String) o,
							BUILD_DEFAULT_ENDSWITH);
					continue;
				}
			}
			addQuotes = false;
		}
		return sb.toString();
	}

	private static final boolean endsWith_array(String s, String[] possibilites) {
		boolean yes = false;
		for (String check : possibilites) {
			yes |= s.endsWith(check);
		}
		return yes;
	}
}
