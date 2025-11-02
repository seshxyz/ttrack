package com.thiscompany.ttrack.core;

import java.util.Collection;
import java.util.function.Supplier;

public class SingleCollectionContainer<E extends Collection<?>> {
	
	private final Supplier<E> factory;
	
	private E object;
	
	public SingleCollectionContainer(Supplier<E> factory) {
		this.factory = factory;
	}
	
	public E getOrCreate() {
		E temp = object;
		return temp != null ? temp : factory.get();
	}
	
	public void clearAndRelease() {
		if (this.object != null) {
			this.object.clear();
		}
	}
	
}
