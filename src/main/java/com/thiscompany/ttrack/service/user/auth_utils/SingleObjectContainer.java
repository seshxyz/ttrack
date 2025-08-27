package com.thiscompany.ttrack.service.user.auth_utils;

import java.util.Collection;
import java.util.function.Supplier;

public class SingleObjectContainer<E extends Collection<?>> {

    private final Supplier<E> factory;
    private E object;

    public SingleObjectContainer(Supplier<E> factory) {
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
