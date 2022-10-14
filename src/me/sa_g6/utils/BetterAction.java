package me.sa_g6.utils;

import me.sa_g6.iterator.ElementIterator;
import me.sa_g6.ui.widgets.EnhancedHTMLDocument;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.*;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.undo.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;

public class BetterAction {
    public static void insertHtml(JTextPane editor, int offset, String html){
        insertHtml(editor,offset,html,null);
    }

    public static void insertHtml(JTextPane editor, int offset, String html, HTML.Tag tag){
        HTMLEditorKit kit = (HTMLEditorKit) editor.getEditorKit();
        HTMLDocument doc = (HTMLDocument) editor.getStyledDocument();

        try {
            kit.insertHTML(doc, offset, html, 0, 0, tag);
        } catch (BadLocationException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void insertImage(JTextPane editor, int offset, BufferedImage image){
        URL url = ImageUtils.putImage(image);
        EnhancedHTMLDocument doc = (EnhancedHTMLDocument) editor.getDocument();
        doc.startEdit();
        insertHtml(editor,offset, "<img src=\"%s\" width=\"%d\" height=\"%d\" style=\"display:block\">>".formatted(url, image.getWidth(), image.getHeight())
            ,mayHasNewLine(editor, offset) ? null : HTML.Tag.IMG);
        doc.finishEdit();
    }

    //<div display: "inline-block">

    public static boolean mayHasNewLine(JTextPane editor, int offset){
        try {
            return editor.getText(offset-1,1).equals("\n");
        } catch (BadLocationException e) {
        }
        return false;
    }

    static JTextPane getEditor(ActionEvent e, Prov<JTextPane> prov){
        if (e.getSource() instanceof JTextPane editor){
            return editor;
        }else if(prov != null){
            return prov.get();
        }
        return null;
    }

    public static class PasteAction extends DefaultEditorKit.PasteAction{
        Prov<JTextPane> prov;
        public PasteAction(){
            this(null);
        }
        public PasteAction(Prov<JTextPane> prov){
            super();
            this.prov = prov;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JTextPane editor;
            if ((editor = getEditor(e, prov)) != null) {
                EnhancedHTMLDocument doc = (EnhancedHTMLDocument) editor.getDocument();
                Clipboard cb = ClipboardUtils.getClipboard();
                Transferable trans = cb.getContents(null);
                try {
                    if(trans.isDataFlavorSupported(DataFlavor.stringFlavor)){
                        String data = (String) trans.getTransferData(DataFlavor.stringFlavor);
                        System.out.println(data);
                        int pos = editor.getCaretPosition();
                        doc.compoundEdit = new CompoundEdit();
                        insertHtml(editor, pos,data.replaceAll("\n","</br>"));
                        if(editor.getCaretPosition() - pos > 1){
                            if(doc.getText(editor.getCaretPosition()-1,1).equals("\n")){
                                doc.remove(editor.getCaretPosition()-1,1);
                            }
                            if(doc.getText(pos,1).equals("\n")){
                                doc.remove(pos,1);
                            }
                        }
                    } else if(trans.isDataFlavorSupported(DataFlavor.imageFlavor)){
                        if(trans.getTransferData(DataFlavor.imageFlavor) instanceof BufferedImage bi){
                            doc.compoundEdit = new CompoundEdit();

                            insertImage(editor, editor.getCaretPosition(), bi);
                        }
                    }
                } catch (IOException | UnsupportedFlavorException | BadLocationException ignore) {
                } finally {
                    doc.finishEdit();
                }
            }
        }
    }

    public static class CopyAction extends DefaultEditorKit.CopyAction{
        Prov<JTextPane> prov;
        public CopyAction(){
            this(null);
        }
        public CopyAction(Prov<JTextPane> prov){
            super();
            this.prov = prov;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            JTextPane editor;
            if ((editor = getEditor(e, prov)) != null) {
                try {
                    super.actionPerformed(e);
                    StringWriter writer = new StringWriter();
                    editor.getEditorKit().write(writer,editor.getDocument(),editor.getSelectionStart(), editor.getSelectedText().length());
                    String data = writer.toString();
                    data = data.substring(20,data.length()-19);
                    ClipboardUtils.getClipboard().setContents(new ClipboardUtils.HTMLTransferable(data), null);
                } catch (IOException | BadLocationException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public static class UndoAction extends AbstractAction{
        UndoManager manager;
        public UndoAction(UndoManager manager){
            this.manager = manager;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                manager.undo();
            }catch (CannotUndoException ex){
            }
        }
    }

    public static class RedoAction extends AbstractAction{
        UndoManager manager;
        public RedoAction(UndoManager manager){
            this.manager = manager;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                manager.redo();
            }catch (CannotRedoException ex){}
        }
    }

    public static class UndoManager extends AbstractUndoableEdit implements UndoableEditListener{
        String lastEditName = null;
        ArrayList<UndoableEdit> edits = new ArrayList<>();
        MyCompoundEdit current;
        int pointer=-1;
        boolean pause = false;
        public UndoManager(){
            super();
        }
        @Override
        public void undoableEditHappened(UndoableEditEvent e) {
            if(pause){
                return;
            }
            if (e.getEdit() instanceof AbstractDocument.DefaultDocumentEvent edit) {
                try {
                    int start = edit.getOffset();
                    int len = edit.getLength();

                    String text = edit.getDocument().getText(start, len);
                    boolean isNeedStart = false;
                    if (current == null) {
                        isNeedStart = true;
                    } else if (text.contains("\n")) {
                        isNeedStart = true;
                    } else if (lastEditName == null || !lastEditName.equals(edit.getPresentationName())) {
                        isNeedStart = true;
                    }

                    while (pointer < edits.size() - 1) {
                        edits.remove(edits.size() - 1);
                        isNeedStart = true;
                    }
                    if (isNeedStart) {
                        createCompoundEdit();
                    }

                    current.addEdit(edit);
                    lastEditName = edit.getPresentationName();
                } catch (BadLocationException e1) {
                }
            }else {
                edits.add(e.getEdit());
                pointer++;
                current = null;
            }
        }

        public void createCompoundEdit() {
            if (current==null || current.getLength()>0) {
                current= new MyCompoundEdit();
            }

            edits.add(current);
            pointer++;
        }

        public void undo() throws CannotUndoException {
            if (!canUndo()) {
                throw new CannotUndoException();
            }

            UndoableEdit u = edits.get(pointer);
            u.undo();
            pointer--;
        }

        public void redo() throws CannotUndoException {
            if (!canRedo()) {
                throw new CannotUndoException();
            }

            pointer++;
            UndoableEdit u = edits.get(pointer);
            u.redo();
        }

        public boolean canUndo() {
            return pointer >= 0;
        }

        public boolean canRedo() {
            return edits.size() > 0 && pointer < edits.size() - 1;
        }

        public void remove(int i){
            edits.remove(i);
        }

        public void pause(){
            pause = true;
        }

        public void resume(){
            pause = false;
        }
    }

    static class MyCompoundEdit extends CompoundEdit {
        boolean isUnDone = false;
        public int getLength() {
            return edits.size();
        }

        public void undo() throws CannotUndoException {
            super.undo();
            isUnDone = true;
        }
        public void redo() throws CannotUndoException {
            super.redo();
            isUnDone = false;
        }
        public boolean canUndo() {
            return edits.size() > 0 && !isUnDone;
        }

        public boolean canRedo() {
            return edits.size() > 0 && isUnDone;
        }
    }

    public static class AttributesChangeEdit extends AbstractUndoableEdit{
        AttributeSet before, after;
        Element element;
        public AttributesChangeEdit(Element e, AttributeSet before){
            this.before = before;
            after = e.getAttributes().copyAttributes();
            element = e;
        }
        @Override
        public void undo() throws CannotUndoException {
            super.undo();
            HTMLDocument.RunElement current = (HTMLDocument.RunElement) element.getAttributes();
            current.removeAttributes(after);
            current.addAttributes(before);
        }

        @Override
        public void redo() throws CannotRedoException {
            super.redo();
            HTMLDocument.RunElement current = (HTMLDocument.RunElement) element;
            current.removeAttributes(before);
            current.addAttributes(after);
        }

        @Override
        public String getPresentationName() {
            return "Attribute Change";
        }
    }
}
