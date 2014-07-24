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
            storage.add(MatchInfo.saveState(match, storage.size()));
        }
        generated = storage.toArray(generated);
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
