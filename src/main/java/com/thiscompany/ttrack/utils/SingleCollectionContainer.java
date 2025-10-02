package com.thiscompany.ttrack.utils;

import java.util.Collection;
import java.util.function.Supplier;

public class SingleCollectionContainer<E extends Collection<?>> {
	
	private final Supplier<E> factory;
	
	private E object;
	
	public SingleCollectionContainer(Supplier<E> factory) {
		this.factory = factory;
	}
	
	public E acquire() {
		E temp = object;
		return temp != null ? temp : factory.get();
	}
	
	public void release(E collection) {
		collection.clear();
		object = collection;
	}
	
}
