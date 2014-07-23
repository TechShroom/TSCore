package com.techshroom.tscore.math.processor;

import static com.techshroom.tscore.math.processor.RegexPatterns.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import com.techshroom.tscore.math.exceptions.EvalException;
import com.techshroom.tscore.math.exceptions.EvalException.Reason;
import com.techshroom.tscore.math.operator.Operator;
import com.techshroom.tscore.regex.MatchInfo;

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
		if (numbers.lookingAt()) {
			// first is number
			// op next
			if (!opget.lookingAt()) {
				throw new EvalException(Reason.NEEDED_OPERATOR);
			}
			
			MatchInfo opMatchInfo = MatchInfo.saveState(opget);
		}
	}

	protected abstract void preprocess();

	public abstract BigDecimal process();
}
