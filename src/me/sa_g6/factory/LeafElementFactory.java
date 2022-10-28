package me.sa_g6.factory;

import me.sa_g6.builder.AbstractElementBuilder;
import me.sa_g6.builder.LeafElementBuilder;
import me.sa_g6.ui.widgets.EnhancedHTMLDocument;

import javax.swing.text.AbstractDocument;

public class LeafElementFactory extends AbstractElementFactory{
    public LeafElementFactory(EnhancedHTMLDocument document) {
        super(document);
    }

    @Override
    public AbstractDocument.AbstractElement create(AbstractElementBuilder builder) {
        LeafElementBuilder leafBuilder = (LeafElementBuilder) builder;

        return document.hackCreateLeafElement(builder.getParent(), leafBuilder.getP0(), leafBuilder.getP1());
    }
}
