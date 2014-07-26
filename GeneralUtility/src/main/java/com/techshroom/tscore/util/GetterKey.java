package com.techshroom.tscore.util;

public final class GetterKey<T> implements UncheckedCaster<T> {
    private final String name;

    public GetterKey(String tag) {
        name = tag;
    }

    public String name() {
        return name;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T cast(Object o) {
        return (T) o;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("Key<").append(name).append(">")
                .toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj instanceof GetterKey<?>) {
            GetterKey<?> key = (GetterKey<?>) obj;
            return key.name.equals(name);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
