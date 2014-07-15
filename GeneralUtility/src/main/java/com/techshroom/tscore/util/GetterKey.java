package com.techshroom.tscore.util;

public final class GetterKey<T> implements UncheckedCaster<T> {
	private final String name;

	public GetterKey(String tag) {
		name = tag;
	}

	public String name() {
		return name;
	}

	@SuppressWarnings("unchecked")
	public T cast(Object o) {
		return (T) o;
	}
}
