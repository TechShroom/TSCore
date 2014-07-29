package com.techshroom.tscore.math.processor.token;

import java.math.BigDecimal;

public class NumberToken extends BasicToken {
    private final BigDecimal value;

    public NumberToken(BigDecimal val) {
        super(val.toPlainString(), TokenFlag.LIST);
        value = val;
    }

    public BigDecimal getBigDecimal() {
        return value;
    }
}
