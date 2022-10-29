package me.sa_g6.database;

import me.sa_g6.adapter.AbstractElementAdapter;
import me.sa_g6.builder.AbstractElementBuilder;
import me.sa_g6.builder.BranchElementBuilder;
import me.sa_g6.builder.LeafElementBuilder;
import me.sa_g6.factory.BranchElementFactory;
import me.sa_g6.factory.LeafElementFactory;
import me.sa_g6.iterator.ElementIterator;
import me.sa_g6.ui.widgets.EnhancedHTMLDocument;

import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.html.HTML;
import java.util.*;

public class ElementManager{
    IDBManager dbManager;
    public static ElementManager INSTANCE;

    private static final HashMap<EnhancedHTMLDocument, BranchElementFactory> branchFactories = new HashMap<>();
    private static final HashMap<EnhancedHTMLDocument, LeafElementFactory> leafFactories = new HashMap<>();

    public ElementManager(IDBManager dbManager){
        this.dbManager = dbManager;
    }

    public long saveElement(AbstractDocument.AbstractElement element) {
        AbstractElementAdapter adapter = new AbstractElementAdapter(element);
        if(element instanceof AbstractDocument.BranchElement){
            List<Long> children = new ArrayList<>();
            AbstractDocument.AbstractElement child;
            for(ElementIterator iter = new ElementIterator(element) ; iter.hasNext();){
                child = (AbstractDocument.AbstractElement) iter.next();
                children.add(saveElement(child));
            }
            adapter.setChildren(children);
        } else if(element instanceof AbstractDocument.LeafElement){
            adapter.setStartPosition(element.getStartOffset());
            adapter.setEndPosition(element.getEndOffset());
        }else {
            return -1;
        }
        AttributeSet attrs = element.getAttributes();
        Map<Object, Object> map = new HashMap<>();
        for (Iterator<?> it = attrs.getAttributeNames().asIterator(); it.hasNext(); ){
            Object key = it.next();
            Object value = attrs.getAttribute(key);
            map.put(key, value);
        }
        adapter.setAttrs(map);

        return dbManager.save(adapter);
    }

    public List<Long> saveParagraphElement(Element element){
        List<Long> children = new ArrayList<>();
        ElementIterator iter = new ElementIterator(element);
        AbstractDocument.AbstractElement child = null;
        for(; iter.hasNext();){
            child = (AbstractDocument.AbstractElement) iter.next();
            children.add(saveElement(child));
        }
        return children;
    }

    public BranchElementFactory getBranchFactory(EnhancedHTMLDocument document){
        if(!branchFactories.containsKey(document)){
            BranchElementFactory factory = new BranchElementFactory(document);
            branchFactories.put(document, factory);
            return factory;
        }
        return branchFactories.get(document);
    }

    public LeafElementFactory getLeafFactory(EnhancedHTMLDocument document){
        if(!leafFactories.containsKey(document)){
            LeafElementFactory factory = new LeafElementFactory(document);
            leafFactories.put(document, factory);
            return factory;
        }
        return leafFactories.get(document);
    }

    public Element getElement(EnhancedHTMLDocument document, long id){
        return getElement(document, document.getDefaultRootElement(), id);
    }

    public AbstractDocument.AbstractElement getElement(EnhancedHTMLDocument document, Element parent, long id) {
        AbstractElementAdapter adapter = dbManager.get(id);
        AbstractElementBuilder builder;
        if(adapter.getType().equals(ElementType.BRANCH)){
            builder = new BranchElementBuilder(document);
        }else {
            builder = new LeafElementBuilder(document);
        }
        builder.setParent(parent);
        builder.setAdapter(adapter);
        adapter.getObjectAttrs().forEach(builder::addAttribute);
        if(adapter.getType().equals(ElementType.BRANCH)){
            builder.setFactory(getBranchFactory(document));
            AbstractDocument.BranchElement element = (AbstractDocument.BranchElement) builder.build();

            List<AbstractDocument.AbstractElement> children = new ArrayList<>();
            for(Iterator<Long> iter = adapter.createIterator(); iter.hasNext();){
                children.add(getElement(document, element, iter.next()));
            }
            element.replace(0,element.getElementCount(), children.toArray(new AbstractDocument.AbstractElement[0]));

            return element;
        }else{
            LeafElementBuilder leafBuilder = (LeafElementBuilder) builder;
            builder.setFactory(getLeafFactory(document));
            leafBuilder.setPosition(adapter.getStartPosition(), adapter.getEndPosition());
            return builder.build();
        }
    }
}
