package com.techshroom.tscore.math.processor;

import java.util.regex.Pattern;

public final class RegexPatterns {
	/**
	 * Regex to find any numbers in a string.<br>
	 * <br>
	 * 
	 * <code>REGEX=([-+]?[0-9]+(\.[0-9]+)?([eE][-+]?[0-9]+)?)</code>
	 */
	protected static final Pattern NUMBER_REGEX = Pattern
			.compile(RegexBits.NUMBER_BASE);
	/**
	 * Regex to validate functions.<br>
	 * <br>
	 * 
	 * <code>REGEX=(([a-zA-Z][a-zA-Z0-9]*)\(((?:@[a-zA-Z][a-zA-Z0-9]*)?(?:,\s*(?:@[a-zA-Z][a-zA-Z0-9]*))*)\))</code>
	 */
	public static final Pattern FUNCTION_VALIDATE_REGEX = Pattern.compile("("
			+ RegexBits.FUNCTION + RegexBits.OPTSPACE + "\\(("
			+ RegexBits.OPTSPACE + RegexBits.VARIABLE + "?(?:"
			+ RegexBits.OPTSPACE + "," + RegexBits.OPTSPACE
			+ RegexBits.VARIABLE + ")*)" + RegexBits.OPTSPACE + "\\))");
	/**
	 * Regex to find functions.<br>
	 * <br>
	 * 
	 * <code>REGEX=(([a-zA-Z][a-zA-Z0-9]*)\(((?:([-+]?\\d+(\\.\\d+)?([eE][-+]?\\d+)?))?(?:,\s*(?:([-+]?\\d+(\\.\\d+)?([eE][-+]?\\d+)?)))*)\))</code>
	 */
	public static final Pattern FUNCTION_GET_REGEX = Pattern.compile("("
			+ RegexBits.FUNCTION + RegexBits.OPTSPACE + "\\(("
			+ RegexBits.OPTSPACE + RegexBits.NUMBER_BASE + "?(?:"
			+ RegexBits.OPTSPACE + "," + RegexBits.OPTSPACE
			+ RegexBits.NUMBER_BASE + ")*)" + RegexBits.OPTSPACE + "\\))");
	/**
	 * Regex to validate operators.<br>
	 * <br>
	 * 
	 * <code>REGEX=(((?:@[a-zA-Z][a-zA-Z0-9]*))?[^a-zA-Z0-9]((?:@[a-zA-Z][a-zA-Z0-9]*))?)</code>
	 */
	public static final Pattern OPERATOR_VALIDATE_REGEX = Pattern.compile("(("
			+ RegexBits.VARIABLE + ")?" + RegexBits.OPERATOR + "("
			+ RegexBits.VARIABLE + ")?)");
	/**
	 * Regex to get operators.<br>
	 * <br>
	 * 
	 * <code>REGEX=([^a-zA-Z0-9])</code>
	 */
	public static final Pattern OPERATOR_GET_REGEX = Pattern.compile("("
			+ RegexBits.OPERATOR + ")");

	private RegexPatterns() {
	}
}
