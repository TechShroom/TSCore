package com.techshroom.tscore.regex;

import java.util.regex.MatchResult;

public interface IExtendedMatcher extends MatchResult {
    MatchInfo lookingAt(int start);

    MatchInfo matches(int start);
}
