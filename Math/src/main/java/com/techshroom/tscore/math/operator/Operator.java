package com.techshroom.tscore.math.operator;

import java.util.*;

import com.techshroom.tscore.util.QuickStringBuilder;

public final class Operator {
	private static final class OperatorLookupKey {
		private final NumberPlacement placement;
		private final Associativeness assoc;
		private final int priority;
		private final String token;

		private OperatorLookupKey(NumberPlacement plcmnt, Associativeness assc,
				int pri, String tkn) {
			placement = plcmnt;
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
				return olk.placement == placement && olk.priority == priority
						&& olk.assoc == assoc
						&& token.equalsIgnoreCase(olk.token);
			}
			return false;
		}

		@Override
		public int hashCode() {
			return placement.hashCode() + assoc.hashCode()
					/ (priority / token.hashCode());
		}

		@Override
		public String toString() {
			return QuickStringBuilder.build("OpKey<", "placement=", placement,
					",token=", token, ",associativeness=", assoc, ",priority=",
					priority, ">");
		}
	}

	private static final class TknAndPlaceKey {
		private final NumberPlacement placement;
		private final String token;

		private TknAndPlaceKey(NumberPlacement plcmnt, String tkn) {
			placement = plcmnt;
			token = tkn;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj instanceof OperatorLookupKey) {
				OperatorLookupKey olk = (OperatorLookupKey) obj;
				return olk.placement == placement
						&& token.equalsIgnoreCase(olk.token);
			}
			return false;
		}

		@Override
		public int hashCode() {
			return placement.hashCode() + token.hashCode();
		}

		@Override
		public String toString() {
			return QuickStringBuilder.build("OpKey2<", "placement=", placement,
					",token=", token, ">");
		}
	}

	private static final Map<OperatorLookupKey, Operator> lookups = new HashMap<OperatorLookupKey, Operator>();
	private static final Map<TknAndPlaceKey, Operator> tokenLookups = new HashMap<TknAndPlaceKey, Operator>();

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
			Associativeness assoc, NumberPlacement placement) {
		OperatorLookupKey target = new OperatorLookupKey(placement, assoc,
				prio, token);
		TknAndPlaceKey otherTarget = new TknAndPlaceKey(placement, token);
		Operator o = getOperator(token, placement);
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
				// NB: placement isn't handled because it is different.
				sb.append("an unknown error occured");
			}
			throw new IllegalArgumentException(sb.toString());
		}

		// no operator yet: define it
		o = new Operator(target);
		lookups.put(target, o);
		tokenLookups.put(otherTarget, o);
		return getOperator(token, placement);
	}

	public static Operator getOperator(String token,
			NumberPlacement requestedPlacement) {
		return tokenLookups.get(new TknAndPlaceKey(requestedPlacement, token));
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
