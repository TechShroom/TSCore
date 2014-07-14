package com.techshroom.tscore.math;

import static com.techshroom.tscore.math.BIMath.lessThan;

import java.math.BigInteger;

public final class ExtraMath {
	private ExtraMath() {
	}

	/**
	 * Handles BigInteger factorial.
	 * 
	 * @param num1
	 *            - factorial number
	 * @return <tt>num1!</tt>
	 */
	public static BigInteger factorial(BigInteger num1) {
		BigInteger one = BigInteger.ONE, fact = one;
		for (BigInteger i = BigInteger.valueOf(2); lessThan(i, num1); i = i
				.add(one)) {
			fact = fact.multiply(i);
		}
		return fact;
	}
}
