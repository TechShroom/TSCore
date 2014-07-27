package com.techshroom.tscore.math.processor.token;

import java.util.List;

public class ListToken extends BasicToken {
    private final List<Token> tokenList;

    public ListToken(List<Token> val) {
        super(val.toString());
        tokenList = val;
    }

    public List<Token> list() {
        return tokenList;
    }
}
