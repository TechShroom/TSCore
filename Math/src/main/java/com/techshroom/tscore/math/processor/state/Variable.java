package com.techshroom.tscore.math.processor.state;

public class Variable<TYPE> extends OneValueStruct<TYPE> {
    private OneValueStruct<String> name;

    Variable(OneValueStruct<String> nme, TYPE referent) {
        super(referent);
        name = nme;
    }

    Variable(OneValueStruct<String> nme) {
        this(nme, null);
    }

    Variable(String nme, TYPE referent) {
        this(new OneValueStruct<String>(nme), referent);
    }

    Variable(String nme) {
        this(nme, null);
    }

    public String name() {
        return name.data();
    }

    public Variable<TYPE> name(String nme) {
        name.data(nme);
        return this;
    }
}
