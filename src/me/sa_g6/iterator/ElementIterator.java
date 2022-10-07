package me.sa_g6.iterator;

import javax.swing.text.Element;
import java.util.Iterator;

public class ElementIterator implements Iterator<Element> {
    Element parent;
    int index = 0, size;
    public ElementIterator(Element parent){
        this.parent = parent;
        size = parent.getElementCount();
    }
    @Override
    public boolean hasNext() {
        return index < size;
    }

    @Override
    public Element next() {
        return parent.getElement(index++);
    }

    public int find(Element e){
        index = 0;
        while (hasNext()){
            Element curr = next();
            if(e.equals(curr)){
                return index-1;
            }
        }
        return -1;
    }
}
