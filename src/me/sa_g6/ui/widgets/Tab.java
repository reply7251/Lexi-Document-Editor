package me.sa_g6.ui.widgets;

import me.sa_g6.utils.BetterAction;
import me.sa_g6.utils.ClipboardUtils;
import me.sa_g6.utils.CombinedAction;
import me.sa_g6.utils.ImageUtils;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.html.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class Tab extends JPanel {
    JTextPane editor = new JTextPane();
    JPopupMenu popup = new JPopupMenu();

    public Tab(){
        super();
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);

        //editor.setContentType("text/html");
        editor.setDocument(new EnhancedHTMLDocument());
        editor.setEditorKit(new EnhancedHTMLEditorKit());
        ImageUtils.setCache(editor.getDocument());
        insertHtml(0,"<html><body></body></html>");
        HTMLDocument doc = (HTMLDocument) editor.getDocument();
        doc.getStyleSheet().addRule("""
                td {
                    border: 1px solid black;
                    width:100px
                }
                """);
        JScrollPane pane = new JScrollPane(editor);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(pane));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(pane));
        KeyStroke ctrlV = KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK);
        final ActionListener ctrlVAction = editor.getActionForKeyStroke(ctrlV);
        editor.unregisterKeyboardAction(ctrlV);
        editor.registerKeyboardAction(new BetterAction.PasteAction(), ctrlV, JComponent.WHEN_FOCUSED);
        KeyStroke ctrlC = KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK);
        editor.unregisterKeyboardAction(ctrlC);
        editor.registerKeyboardAction(new BetterAction.CopyAction(), ctrlC, JComponent.WHEN_FOCUSED);


        final JMenuItem copy = new JMenuItem("Copy      CTRL+C");
        copy.addActionListener(new BetterAction.CopyAction());

        editor.getDocument().getRootElements();

        popup.add(copy);
        copy.setEnabled(true);
        editor.setComponentPopupMenu(popup);
    }

    public JTextPane getEditor() {
        return editor;
    }

    public HTMLDocument getDocument(){
        return (HTMLDocument) editor.getDocument();
    }

    public void insertHtml(int offset, String html){
        BetterAction.insertHtml(editor, offset, html);
    }

    public void insertTable(int offset, int rowCount, int colCount){
        StringBuilder builder = new StringBuilder("<table>");

        for(int i = 0; i < rowCount; i++){
            builder.append("<tr>");
            builder.append("<td><div></div></td>".repeat(Math.max(0, colCount)));
            builder.append("</tr>\n");
        }

        builder.append("</table>\n");
        insertHtml(offset, builder.toString());
    }

    public void insertImage(int offset, BufferedImage image){
        BetterAction.insertImage(editor, offset, image);
    }
}

class EnhancedHTMLEditorKit extends HTMLEditorKit{
    ImageController imageController = new ImageController();

    @Override
    public void install(JEditorPane c) {
        c.addMouseListener(imageController);
        c.addMouseMotionListener(imageController);
        imageController.setEditor(c);
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

class EnhancedHTMLDocument extends HTMLDocument{
    public EnhancedHTMLDocument(){
        this(new StyleSheet());
    }

    public EnhancedHTMLDocument(StyleSheet ss){
        super(ss);
        setParser(new EnhancedHTMLEditorKit().hackGetParser());
    }

    public void hackWriteLock(){
        writeLock();
    }

    public void hackWriteUnlock(){
        writeUnlock();
    }
}

