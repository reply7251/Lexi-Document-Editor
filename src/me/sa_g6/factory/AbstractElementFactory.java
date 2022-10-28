package me.sa_g6.factory;

import me.sa_g6.adapter.AbstractElementAdapter;
import me.sa_g6.builder.AbstractElementBuilder;
import me.sa_g6.ui.widgets.EnhancedHTMLDocument;

import javax.swing.text.AbstractDocument;
import javax.swing.text.DefaultStyledDocument;

public abstract class AbstractElementFactory {
    EnhancedHTMLDocument document;

    public AbstractElementFactory(EnhancedHTMLDocument document){
        this.document = document;
    }

    public abstract AbstractDocument.AbstractElement create(AbstractElementBuilder builder);
}
