package com.techshroom.tscore.math;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.techshroom.tscore.math.exceptions.EvalException;
import com.techshroom.tscore.math.exceptions.EvalException.Reason;
import com.techshroom.tscore.math.operator.Operator;
import com.techshroom.tscore.regex.MatchInfo;
import com.techshroom.tscore.regex.MatchList;

public abstract class ExpressionProcessor {
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
	protected final String processing;
	protected final List<String> tokens = new ArrayList<String>();

	protected ExpressionProcessor(String proc) throws EvalException {
		processing = proc.trim();
		tokenize();
		preprocess();
	}

	private void tokenize() throws EvalException {
		// we can tokenize:
		// operators: required to tokenize: at least one @ var, at most two,
		// make it a function after that
		// functions can tokenize with something like f(@x) for f(x)
		// numbers are ([-+]?[0-9]+(\.[0-9]+)?([eE][-+]?[0-9]+)?)
		// catches all things
		Matcher numbers = NUMBER_REGEX.matcher(processing);
		Matcher funcget = FUNCTION_GET_REGEX.matcher(processing);
		Matcher opget = OPERATOR_GET_REGEX.matcher(processing);
		// the functions match their numbers as well, be careful about that
		// scan the numbers and mark positions
		// match numbers to operators
		List<MatchInfo> numMatches = new MatchList(numbers);
		List<MatchInfo> funcMatches = new MatchList(funcget);
		List<MatchInfo> opMatches = new MatchList(opget);
		if (numMatches.size() == 0) {
			// no numbers: invalid format
			throw new EvalException(Reason.NO_NUMBERS);
		}
		if (opMatches.size() == 0) {
			// no operators, required:
			// one function or only one number
			if (funcMatches.size() == 0) {
				// no functions, this means there was just one or more numbers.
				if (numMatches.size() == 1) {
					tokens.add(numMatches.get(0).getMatch());
					return;
				} else {
					// too many numbers
					throw new EvalException(Reason.TOO_MANY_NUMBERS, 1,
							numMatches.size());
				}
			}
		}

		// by this point there are some functions or operators to work with
		if (numMatches.get(0).getStart() == 0) {
			// number first, which implies operator first
			// matches are in order from string
			MatchInfo firstOp = opMatches.get(0);
			Operator check = Operator.getOperator(firstOp.getMatch());
			// push a number
			tokens.add(numMatches.get(0).getMatch());
		}
	}

	protected abstract void preprocess();

	public abstract BigDecimal process();
}
