package com.thiscompany.ttrack.core._abstract;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class MapCollector<K,V> {
	
	private final Map<K,V> map;
	private final int sizeLimit;
	private final AtomicInteger counter;
	private final boolean sizeAndCounterInitialized;
	
	public MapCollector(Supplier<Map<K,V>> map, int sizeLimit) {
		this.map = map.get();
		this.sizeLimit = sizeLimit;
		this.counter = new AtomicInteger(0);
		this.sizeAndCounterInitialized = true;
	}
	
	public MapCollector(Supplier<Map<K,V>> map) {
		this.map = map.get();
		this.sizeLimit = 0;
		this.counter = null;
		this.sizeAndCounterInitialized = false;
	}
	
	public void addToBuffer(K key,
							BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
		map.compute(key, remappingFunction);
	}
	
	public void addToBuffer(K key, V value) {
		map.put(key, value);
	}
	
	public void forEach(BiConsumer<? super K, ? super V> action) {
		map.forEach(action);
	}
	
	public void clearMap() {
		map.clear();
		if(counter != null) {
			counter.set(0);
		}
	}
	
	public void incrementCounter() {
		if(sizeAndCounterInitialized) {
			counter.incrementAndGet();
		}
	}
	
	public boolean checkIsMapFull() {
		if(sizeAndCounterInitialized){
			return counter.get() == sizeLimit;
		}
		return false;
	}
	
}
