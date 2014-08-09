package com.techshroom.tscore.math.processor.token;

import com.techshroom.tscore.math.operator.Operator;

public class OperatorToken extends BasicToken {
    private final Operator op;

    public OperatorToken(Operator o) {
        super(o.toString(), TokenFlag.OPERATOR);
        op = o;
    }

    public Operator getOp() {
        return op;
    }
}
