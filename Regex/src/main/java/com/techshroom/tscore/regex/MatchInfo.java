package com.techshroom.tscore.regex;

public final class MatchInfo {
	public static final MatchInfo create(String match, String[] groups,
			int start, int end, int group) {
		return new MatchInfo(match, groups, start, end, group);
	}

	private final String match;
	private final String[] groups;
	private final int start, end, group;

	private MatchInfo(String mtch, String[] grps, int st, int en, int grp) {
		match = mtch;
		start = st;
		end = en;
		group = grp;
		groups = grps;
	}

	public String getMatch() {
		return match;
	}

	public int getStart() {
		return start;
	}

	public int getEnd() {
		return end;
	}

	public int getGroup() {
		return group;
	}

	public String[] getGroups() {
		return groups;
	}
	
	@Override
	public String toString() {
		return "";
	}
}
