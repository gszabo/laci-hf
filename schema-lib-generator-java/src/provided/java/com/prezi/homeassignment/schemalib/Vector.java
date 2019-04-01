package com.prezi.homeassignment.schemalib;

import java.util.Iterator;

/**
 * A mutable ordered collection.
 * The user of this interface has precise control over where in the list each element is inserted.
 * The user can access elements by their integer index, or iterate through all elements.
 * Supports removing an element by index.
 * Does not support null elements.
 */
public interface Vector<T> extends Iterable<T> {
    /**
     * Returns the number of elements in this Vector.
     * @return the number of elements in this Vector
     */
    int size();

    /**
     * Returns the element at the given index.
     * @param index index of element to return
     * @return the element at the given index
     * @throws IndexOutOfBoundsException - if the index is out of range (index < 0 || index >= size())
     */
    T get(int index);

    /**
     * Inserts an element at the given position, and shifts the element at that position and any subsequent elements to the right.
     * @param index the index where the element should be insterted
     * @param t the element to be insterted
     * @return this Vector instance
     * @throws IndexOutOfBoundsException - if the index is out of range (index < 0 || index > size())
     * @throws UnsupportedOperationException - if the element to be insterted is null
     */
    Vector<T> insert(int index, T t);

    /**
     * Removes an element at the given position, shifts all subsequent elements to the left. Returns the removed element.
     * @param index the index of the element
     * @return the removed element
     * @throws IndexOutOfBoundsException - if the index is out of range (index < 0 || index > size())
     */
    T remove(int index);

    /**
     * Returns an iterator that iterates through all elements in the right order (starting from index 0 to size()-1).
     * @return an iterator that iterates through all elements in the right order
     */
    Iterator<T> iterator();
}
