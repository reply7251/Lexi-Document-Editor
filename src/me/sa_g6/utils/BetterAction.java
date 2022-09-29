package me.sa_g6.utils;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;

public class BetterAction {
    public static void insertHtml(JTextPane editor,int offset, String html){
        HTMLEditorKit kit = (HTMLEditorKit) editor.getEditorKit();
        HTMLDocument doc = (HTMLDocument) editor.getStyledDocument();

        try {
            kit.insertHTML(doc, offset, html, 0, 0, null);
        } catch (BadLocationException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void insertImage(JTextPane editor, int offset, BufferedImage image){
        URL url = ImageUtils.putImage(image);
        insertHtml(editor,offset, "<img src=\"%s\" width=\"%d\" height=\"%d\">".formatted(url, image.getWidth(), image.getHeight()));
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
                HTMLDocument doc = (HTMLDocument) editor.getDocument();
                Clipboard cb = ClipboardUtils.getClipboard();
                Transferable trans = cb.getContents(null);
                try {
                    if(trans.isDataFlavorSupported(DataFlavor.stringFlavor)){
                        String data = (String) trans.getTransferData(DataFlavor.stringFlavor);
                        int pos = editor.getCaretPosition();
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
                            int pos = editor.getCaretPosition();
                            insertImage(editor, editor.getCaretPosition(), bi);
                            if(editor.getCaretPosition() - pos > 1){
                                if(doc.getText(editor.getCaretPosition()-1,1).equals("\n")){
                                    doc.remove(editor.getCaretPosition()-1,1);
                                }
                                if(doc.getText(pos,1).equals("\n")){
                                    doc.remove(pos,1);
                                }
                            }
                        }
                    }
                } catch (IOException | UnsupportedFlavorException | BadLocationException ignore) {
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
}
