package com.techshroom.tscore.math.operator;

public enum Associativeness {
	/*
	 * The order of these enums is important for the built in compareTo to work.
	 */
	/**
	 * Operator is left-associative
	 */
	LEFT,
	/**
	 * Operator is not associative
	 */
	NONE,
	/**
	 * Operator is right-associative
	 */
	RIGHT;
}
