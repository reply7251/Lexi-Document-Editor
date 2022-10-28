package me.sa_g6.builder;

import me.sa_g6.adapter.AbstractElementAdapter;
import me.sa_g6.factory.AbstractElementFactory;
import me.sa_g6.ui.widgets.EnhancedHTMLDocument;

import javax.swing.text.AbstractDocument;
import javax.swing.text.Element;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractElementBuilder implements IElementBuilder{
    Map<Object, Object> attrs = new HashMap<>();
    EnhancedHTMLDocument document;
    AbstractElementFactory factory;
    AbstractElementAdapter adapter;

    Element parent;

    public AbstractElementBuilder(EnhancedHTMLDocument document){
        this.document = document;
    }

    public AbstractElementBuilder setParent(Element element){
        parent = element;
        return this;
    }

    public Element getParent() {
        return parent;
    }

    public AbstractElementBuilder setFactory(AbstractElementFactory factory){
        this.factory = factory;
        return this;
    }

    public AbstractElementBuilder setAdapter(AbstractElementAdapter adapter){
        this.adapter = adapter;
        return this;
    }

    public AbstractElementBuilder addAttribute(Object key, Object value){
        attrs.put(key, value);
        return this;
    }

    public Map<Object, Object> getAttrs() {
        return attrs;
    }

    @Override
    public AbstractDocument.AbstractElement build() {
        AbstractDocument.AbstractElement result = factory.create(this);
        getAttrs().forEach((k, v) -> {
            result.addAttribute(k, v);
        });
        return result;
    }
}
