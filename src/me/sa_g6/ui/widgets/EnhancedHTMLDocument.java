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
        this(new GapContent(BUFFER_SIZE_DEFAULT), ss);
    }

    public EnhancedHTMLDocument(Content content){
        this(content, new StyleSheet());
    }

    public EnhancedHTMLDocument(Content content, StyleSheet ss){
        super(content, ss);
        getStyleSheet().addRule("""
                td {
                    border: 1px solid black;
                    width:100px
                }
                """);
        setParser(new EnhancedHTMLEditorKit().hackGetParser());
    }

    public void setBuffer(ElementBuffer buffer){
        this.buffer = buffer;
    }

    public ElementBuffer getBuffer(){
        return buffer;
    }

    public Content hackGetContent(){
        return getContent();
    }

    public void hackInsert(int offset, ElementSpec[] data) throws BadLocationException {
        insert(offset, data);
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

    public BranchElement hackCreateBranchElement(Element parent){
        return (BranchElement) createBranchElement(parent, null);
    }

    public LeafElement hackCreateLeafElement(Element parent, int p0, int p1){
        return (LeafElement) createLeafElement(parent, null, p0, p1);
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

    public static class EnhancedHTMLEditorKit extends HTMLEditorKit {
        ImageController imageController = new ImageController();
        //MouseObererverable mouseEvent = new MouseObererverable();
        MouseEventBus eventBus;

        private final HTMLEditorKit.HTMLFactory factory = new HTMLBetterFactory();

        public void setEventBus(MouseEventBus eventBus){
            this.eventBus=eventBus;
        }
        @Override
        public ViewFactory getViewFactory() {
            return factory;
        }


        @Override
        public void install(JEditorPane c) {
            c.addMouseListener(eventBus);
            c.addMouseMotionListener(eventBus);
            imageController.setEditor((JTextPane)c);
            eventBus.subscribe(imageController);
            super.install(c);
        }

        @Override
        public void deinstall(JEditorPane c) {
            c.removeMouseListener(eventBus);
            c.removeMouseMotionListener(eventBus);
            imageController.setEditor(null);
            eventBus.unsubscribe(imageController);
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
