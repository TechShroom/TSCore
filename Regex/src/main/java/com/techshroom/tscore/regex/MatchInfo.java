package com.techshroom.tscore.regex;

import static com.techshroom.tscore.util.QuickStringBuilder.*;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;

public final class MatchInfo {

    public static final MatchInfo saveState(MatchResult match, boolean matched) {
        return saveState(match, matched, -1);
    }

    public static final MatchInfo saveState(MatchResult match, boolean matched,
            int index) {
        MatchResult m = match;
        if (m instanceof Matcher) {
            m = ((Matcher) match).toMatchResult();
        }
        return new MatchInfo(m, index, matched);
    }

    private static String[] grabGroups(MatchResult match) {
        if (!success(match)) {
            return new String[] {};
        }
        String[] ret = new String[match.groupCount()];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = match.group(i);
        }
        return ret;
    }

    private static boolean success(MatchResult m) {
        try {
            m.start();
        } catch (IllegalStateException e) {
            return false;
        }
        return true;
    }

    private final String[] groups;
    private final MatchResult pair;
    private final int index;
    private final boolean success;

    private MatchInfo(MatchResult wrap, int ind, boolean s) {
        groups = grabGroups(wrap);
        pair = wrap;
        index = ind;
        success = s;
    }

    public String getMatch() {
        return pair.group();
    }

    public int getStart() {
        return pair.start();
    }

    public int getEnd() {
        return pair.end();
    }

    public int getIndex() {
        return index;
    }

    public String[] getGroups() {
        return groups;
    }

    public boolean hasMatch() {
        return success;
    }

    @SuppressWarnings("boxing")
    @Override
    public String toString() {
        return concatToStringComplex("MatchInfo<", "match=", getMatch(),
                ",start=", getStart(), ",end=", getEnd(), ",index=", index,
                ",groups[]=", groups, ">");
    }
}
