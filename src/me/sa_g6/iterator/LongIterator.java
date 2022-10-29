package me.sa_g6.iterator;

import me.sa_g6.adapter.AbstractElementAdapter;

import java.util.Iterator;

public class LongIterator implements Iterator<Long> {
    private AbstractElementAdapter adapter;
    int i = 0;

    public LongIterator(AbstractElementAdapter adapter){
        this.adapter = adapter;
    }

    @Override
    public boolean hasNext() {
        return i < adapter.getChildren().size();
    }

    @Override
    public Long next() {
        return adapter.getChildren().get(i++);
    }
}
