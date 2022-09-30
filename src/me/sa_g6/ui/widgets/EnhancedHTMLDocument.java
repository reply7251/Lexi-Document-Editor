package me.sa_g6.ui.widgets;

import me.sa_g6.utils.BetterAction;

import javax.swing.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
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

    @Override
    protected void fireUndoableEditUpdate(UndoableEditEvent e) {
        if (compoundEdit == null) {
            super.fireUndoableEditUpdate(e);
        } else {
            compoundEdit.addEdit(e.getEdit());
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

    static class EnhancedHTMLEditorKit extends HTMLEditorKit {
        ImageController imageController = new ImageController();

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
