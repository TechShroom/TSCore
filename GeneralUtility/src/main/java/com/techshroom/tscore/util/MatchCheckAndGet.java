package com.techshroom.tscore.util;

public interface MatchCheckAndGet<INPUT, OUTPUT> extends MatchChecker<INPUT>,
        Getter<OUTPUT> {
    OUTPUT getIfMatches(INPUT expr);
}
