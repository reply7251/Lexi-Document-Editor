package me.sa_g6.builder;

import me.sa_g6.ui.widgets.EnhancedHTMLDocument;

import javax.swing.text.AbstractDocument;
import javax.swing.text.Element;

public class LeafElementBuilder extends AbstractElementBuilder{
    int p0, p1;

    public LeafElementBuilder(EnhancedHTMLDocument document) {
        super(document);
    }

    public int getP0() {
        return p0;
    }

    public int getP1() {
        return p1;
    }

    public LeafElementBuilder setPosition(int p0, int p1) {
        this.p0 = p0;
        this.p1 = p1;
        return this;
    }
}
