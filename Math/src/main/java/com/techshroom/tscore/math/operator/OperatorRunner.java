package com.techshroom.tscore.math.operator;

import java.math.BigDecimal;

public interface OperatorRunner {
    long runLong(long... longs);

    double runDouble(double... doubles);

    BigDecimal runBigDec(BigDecimal... bigdecimals);
}
