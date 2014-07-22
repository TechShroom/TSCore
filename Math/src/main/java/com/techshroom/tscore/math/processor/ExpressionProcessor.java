package com.techshroom.tscore.math.processor;

import static com.techshroom.tscore.math.processor.RegexPatterns.FUNCTION_GET_REGEX;
import static com.techshroom.tscore.math.processor.RegexPatterns.NUMBER_REGEX;
import static com.techshroom.tscore.math.processor.RegexPatterns.OPERATOR_GET_REGEX;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import com.techshroom.tscore.math.exceptions.EvalException;
import com.techshroom.tscore.math.exceptions.EvalException.Reason;
import com.techshroom.tscore.math.operator.Operator;
import com.techshroom.tscore.regex.MatchInfo;
import com.techshroom.tscore.regex.MatchList;

public abstract class ExpressionProcessor {
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

		for (MatchInfo opMatch : opMatches) {
			System.err.println(opMatch);
		}

		// by this point there are some functions or operators to work with
		if (numMatches.get(0).getStart() == 0) {
			// number first, which implies operator first
			// matches are in order from string
			MatchInfo firstOp = opMatches.get(0);
			Operator check = Operator.getOperator(firstOp.getMatch());
			// push a number
			tokens.add(numMatches.get(0).getMatch());
			tokens.add(check.toString());
			if (numMatches.get(1).getStart() == firstOp.getEnd()) {
				// not function
				tokens.add(numMatches.get(1).getMatch());
			}
		}
	}

	protected abstract void preprocess();

	public abstract BigDecimal process();
}
