package com.techshroom.tscore.math.processor.token;

public class BasicToken implements Token {
    private final String value;

    public BasicToken(String val) {
        value = val;
    }

    @Override
    public String value() {
        return value;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof BasicToken) {
            return ((BasicToken) obj).value.equals(value);
        }
        return false;
    }
    
    @Override
    public String toString() {
        return value;
    }
}
