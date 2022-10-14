package me.sa_g6.ui.widgets;

import me.sa_g6.ui.view.HideableImageView;
import me.sa_g6.utils.BetterAction;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.*;
import javax.swing.text.html.*;
import javax.swing.undo.CompoundEdit;

public class EnhancedHTMLDocument extends HTMLDocument {
    public CompoundEdit compoundEdit;
    BetterAction.UndoManager undoManager;

    public EnhancedHTMLDocument() {
        this(new StyleSheet());
    }

    public EnhancedHTMLDocument(StyleSheet ss) {
        super(ss);
        setParser(new EnhancedHTMLEditorKit().hackGetParser());
    }

    public void hackWriteLock() {
        writeLock();
    }

    public void hackWriteUnlock() {
        writeUnlock();
    }

    public void hackFireChangedUpdate(DocumentEvent e){
        fireChangedUpdate(e);
    }

    @Override
    protected void fireUndoableEditUpdate(UndoableEditEvent e) {
        if (compoundEdit == null) {
            super.fireUndoableEditUpdate(e);
        } else {
            compoundEdit.addEdit(e.getEdit());
        }
    }

    public void startEdit(){
        if(compoundEdit == null){
            compoundEdit = new CompoundEdit();
        }
    }

    public void finishEdit() {
        if (compoundEdit != null) {
            super.fireUndoableEditUpdate(new UndoableEditEvent(this, compoundEdit));
            compoundEdit.end();
            compoundEdit = null;
        }
    }

    @Override
    public void addUndoableEditListener(UndoableEditListener listener) {
        super.addUndoableEditListener(listener);
        if (listener instanceof BetterAction.UndoManager m) {
            undoManager = m;
        }
    }

    static class HTMLBetterFactory extends HTMLEditorKit.HTMLFactory{
        public View create(Element elem) {
            AttributeSet attrs = elem.getAttributes();
            if(elem.getName().equals("img")){
                return new HideableImageView(elem);
            }
            return super.create(elem);
        }
    }

    static class EnhancedHTMLEditorKit extends HTMLEditorKit {
        ImageController imageController = new ImageController();

        private final HTMLEditorKit.HTMLFactory factory = new HTMLBetterFactory();
        @Override
        public ViewFactory getViewFactory() {
            return factory;
        }

        @Override
        public void install(JEditorPane c) {
            c.addMouseListener(imageController);
            c.addMouseMotionListener(imageController);
            imageController.setEditor((JTextPane)c);
            super.install(c);
        }

        @Override
        public void deinstall(JEditorPane c) {
            c.removeMouseListener(imageController);
            c.removeMouseMotionListener(imageController);
            imageController.setEditor(null);
            super.deinstall(c);
        }

        @Override
        public Document createDefaultDocument() {
            return new EnhancedHTMLDocument(getStyleSheet());
        }

        public Parser hackGetParser(){
            return getParser();
        }
    }
}
