package com.thiscompany.ttrack.core.interfaces;

public interface BufferOperator<T, E> {
	
	boolean addToBuffer(T object);
	
	E transformCollection();
	
	boolean isCollectionFull();
	
}
