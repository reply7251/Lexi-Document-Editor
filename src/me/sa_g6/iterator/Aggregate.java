package me.sa_g6.iterator;

import java.util.Iterator;

public interface Aggregate<T> {
    Iterator<T> createIterator();
}
