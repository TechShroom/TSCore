package com.techshroom.tscore.math.processor.state;

public class OneValueStruct<TYPE> {
    private TYPE data;

    public OneValueStruct() {
        this(null);
    }

    public OneValueStruct(TYPE dta) {
        data(dta);
    }

    public TYPE data() {
        return data;
    }

    public TYPE data(TYPE dta) {
        data = dta;
        return data();
    }
}
