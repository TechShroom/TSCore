package com.techshroom.tscore.math.operator;

import java.util.*;

import com.techshroom.tscore.util.QuickStringBuilder;

public final class Operator {
	private static final class OperatorLookupKey {
		private final Associativeness assoc;
		private final int priority;
		private final String token;

		private OperatorLookupKey(Associativeness assc, int pri, String tkn) {
			assoc = assc;
			priority = pri;
			token = tkn;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj instanceof OperatorLookupKey) {
				OperatorLookupKey olk = (OperatorLookupKey) obj;
				//
				return olk.priority == priority && olk.assoc == assoc
						&& token.equalsIgnoreCase(olk.token);
			}
			return false;
		}

		@Override
		public int hashCode() {
			return assoc.hashCode() / (priority / token.hashCode());
		}

		@Override
		public String toString() {
			return QuickStringBuilder.build("OpKey<", "token=", token,
					",associativeness=", assoc, ",priority=", priority, ">");
		}
	}

	private static final Map<OperatorLookupKey, Operator> lookups = new HashMap<OperatorLookupKey, Operator>();
	private static final Map<String, Operator> tokenLookups = new HashMap<String, Operator>();

	public static final Comparator<Operator> ASSOC_COMPARATOR = new Comparator<Operator>() {
		@Override
		public int compare(Operator o1, Operator o2) {
			return o1.ourKey.assoc.compareTo(o2.ourKey.assoc);
		}
	};
	public static final Comparator<Operator> PRIORITY_COMPARATOR = new Comparator<Operator>() {
		@Override
		public int compare(Operator o1, Operator o2) {
			return o1.ourKey.priority - o2.ourKey.priority;
		}
	};
	public static final Comparator<Operator> TOKEN_COMPARATOR = new Comparator<Operator>() {
		@Override
		public int compare(Operator o1, Operator o2) {
			return o1.ourKey.token.compareTo(o2.ourKey.token);
		}
	};

	public static Operator registerOrGetOperator(String token, int prio,
			Associativeness assoc) {
		OperatorLookupKey target = new OperatorLookupKey(assoc, prio, token);
		Operator o = getOperator(token);
		if (o != null) {
			OperatorLookupKey olk = o.ourKey;
			if (olk.equals(target)) {
				return o;
			}
			// something mismatched...but the token matches. What do?
			StringBuilder sb = new StringBuilder(
					"Cannot override the operator ").append(token).append(
					" because ");
			if (olk.priority != target.priority) {
				sb.append("the ")
						.append("priority mismatched (should have been ")
						.append(olk.priority).append(" was ")
						.append(target.priority).append(")");
			} else if (olk.assoc != target.assoc) {
				sb.append("the ")
						.append("associativeness mismatched (should have been ")
						.append(olk.assoc).append(" was ").append(target.assoc)
						.append(")");
			} else {
				sb.append("an unknown error occured");
			}
			throw new IllegalArgumentException(sb.toString());
		}

		// no operator yet: define it
		o = new Operator(target);
		lookups.put(target, o);
		tokenLookups.put(token, o);
		return getOperator(token);
	}

	public static Operator getOperator(String token) {
		return tokenLookups.get(token);
	}

	private final OperatorLookupKey ourKey;

	private Operator(OperatorLookupKey key) {
		ourKey = key;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj instanceof Operator) {
			Operator o = (Operator) obj;
			return ourKey.equals(o.ourKey);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return ourKey.hashCode();
	}

	@Override
	public String toString() {
		return ourKey.token.replace("OpKey", "Operator");
	}
}
