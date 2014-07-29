package com.techshroom.tscore.math.processor.token;

import static com.techshroom.tscore.util.QuickStringBuilder.*;

public class BasicToken implements Token {
    private final String value;
    private final TokenFlag flag;

    public BasicToken(String val, TokenFlag flg) {
        value = val;
        flag = flg;
    }

    @Override
    public String value() {
        return value;
    }

    public TokenFlag flag() {
        return flag;
    }

    @Override
    public int hashCode() {
        return value.hashCode() - flag.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof BasicToken) {
            return ((BasicToken) obj).value.equals(value)
                    && ((BasicToken) obj).flag.equals(flag);
        }
        return false;
    }

    @Override
    public String toString() {
        return concatToStringComplex(flag, "<", "value=", value, ">");
    }
}
