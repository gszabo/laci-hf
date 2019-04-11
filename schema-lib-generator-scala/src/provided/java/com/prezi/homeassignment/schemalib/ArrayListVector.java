package com.prezi.homeassignment.schemalib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

class ArrayListVector<T> implements Vector<T> {

    private List<T> elements;

    public ArrayListVector(T... tList) {
        elements = new ArrayList<>(Arrays.asList(tList));
    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public T get(int index) {
        return elements.get(index);
    }

    @Override
    public Vector<T> insert(int index, T s) {
        if (s == null) {
            throw new UnsupportedOperationException("element to insert is null");
        }
        elements.add(index, s);
        return this;
    }

    @Override
    public T remove(int index) {
        return elements.remove(index);
    }

    @Override
    public Iterator<T> iterator() {
        return elements.iterator();
    }
}