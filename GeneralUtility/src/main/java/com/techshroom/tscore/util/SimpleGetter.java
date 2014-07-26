package com.techshroom.tscore.util;

public abstract class SimpleGetter<T, ARG> implements Getter<T> {
    private ARG arg;
    private boolean argSetFlag = false;
    private final GetterKey<ARG> argKey = new GetterKey<ARG>("arg");

    @Override
    public <X> void setData(GetterKey<X> key, X value) {
        if (key == argKey) {
            arg = argKey.cast(value);
            argSetFlag = true;
        }
    }

    @Override
    public final T get() {
        if (!argSetFlag) {
            handleUnsetArg();
        }
        return doGet();
    }

    protected abstract T doGet();

    @SuppressWarnings("static-method")
    protected void handleUnsetArg() {
        throw new IllegalStateException("must set argument");
    }

    protected ARG getARG() {
        return arg;
    }

    public GetterKey<ARG> key() {
        return argKey;
    }
}
