package com.techshroom.tscore.regex;

import java.util.*;
import java.util.regex.Matcher;

public class MatchList extends AbstractList<MatchInfo> {
	private final Matcher match;
	private MatchInfo[] generated = {};

	public MatchList(Matcher m) {
		match = m;
		generate();
	}

	private void generate() {
		List<MatchInfo> storage = new ArrayList<MatchInfo>();
		while (match.find()) {
			String lock = match.group();
			int start = match.start();
			int end = match.end();
			String[] groups = grabGroups();
			storage.add(MatchInfo.create(lock, groups, start, end,
					storage.size()));
		}
		generated = storage.toArray(generated);
	}

	private String[] grabGroups() {
		String[] ret = new String[match.groupCount()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = match.group(i);
		}
		return ret;
	}

	@Override
	public MatchInfo get(int index) {
		return generated[index];
	}

	@Override
	public int size() {
		return generated.length;
	}
}
