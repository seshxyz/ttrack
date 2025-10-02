package com.thiscompany.ttrack.enums;

public interface GenericEnumFunction<T extends Enum<T>> {
	
	default T getNextFrom(T t) {
		T[] values = t.getDeclaringClass().getEnumConstants();
		if (t.ordinal() == values.length - 1) {
			return t;
		} else return values[t.ordinal() + 1];
	}
	
}
