package com.techshroom.tscore.regex;

import java.util.Arrays;
import java.util.regex.Matcher;

import com.techshroom.tscore.util.QuickStringBuilder;

public final class MatchInfo {
	public static final MatchInfo create(String match, String[] groups,
			int start, int end, int group) {
		return new MatchInfo(match, groups, start, end, group);
	}

	public static final MatchInfo saveState(Matcher match) {
		return saveState(match, -1);
	}

	public static final MatchInfo saveState(Matcher match, int index) {
		String lock = match.group();
		int start = match.start();
		int end = match.end();
		String[] groups = grabGroups(match);
		return create(lock, groups, start, end, index);
	}

	private static String[] grabGroups(Matcher match) {
		String[] ret = new String[match.groupCount()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = match.group(i);
		}
		return ret;
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
		return QuickStringBuilder.build("MatchInfo<", "match=", match,
				",start=", start, ",end=", end, ",group=", group, ",groups[]=",
				Arrays.toString(groups), ">");
	}
}
