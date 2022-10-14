package me.sa_g6.iterator;

import javax.swing.text.Element;
import java.util.Iterator;

public class ElementTreeIterator implements Iterator<Element> {
    Element parent;
    int index = 0, size;
    Iterator<Element> iter = null;
    public ElementTreeIterator(Element parent){
        this.parent = parent;
        size = parent.getElementCount();
    }
    @Override
    public boolean hasNext() {
        return (iter != null && iter.hasNext()) || index < size;
    }

    @Override
    public Element next() {
        if(iter != null && iter.hasNext()){
            return iter.next();
        }
        if(hasNext()){
            Element curr = parent.getElement(index++);
            iter = new ElementTreeIterator(curr);
            return curr;
        }
        return null;
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
