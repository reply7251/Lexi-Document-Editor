package me.sa_g6.factory;

import me.sa_g6.adapter.AbstractElementAdapter;
import me.sa_g6.builder.AbstractElementBuilder;
import me.sa_g6.builder.BranchElementBuilder;
import me.sa_g6.ui.widgets.EnhancedHTMLDocument;

import javax.swing.text.AbstractDocument;
import javax.swing.text.DefaultStyledDocument;

public class BranchElementFactory extends AbstractElementFactory{
    public BranchElementFactory(EnhancedHTMLDocument document) {
        super(document);
    }

    @Override
    public AbstractDocument.AbstractElement create(AbstractElementBuilder builder) {

        return document.hackCreateBranchElement(builder.getParent());
    }
}
