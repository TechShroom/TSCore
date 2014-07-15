package com.techshroom.tscore.util;

public interface Getter<T> {
	<X> void setData(GetterKey<X> key, X value);

	T get();
}
