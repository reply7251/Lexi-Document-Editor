package me.sa_g6.ui.widgets;

import me.sa_g6.utils.ClipboardUtils;
import me.sa_g6.utils.CombinedAction;
import me.sa_g6.utils.ImageUtils;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
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

        editor.setContentType("text/html");
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
        editor.registerKeyboardAction(new CombinedAction(ctrlVAction, (e)->{
            Object src = e.getSource();
            if (src instanceof JComponent c) {
                Clipboard cb = c.getToolkit().getSystemClipboard();
                Transferable trans = cb.getContents(null);
                for(DataFlavor dataFlavor : trans.getTransferDataFlavors()){
                    try {
                        Object o = trans.getTransferData(dataFlavor);
                        if(o instanceof BufferedImage bi){
                            insertImage(editor.getCaretPosition(), bi);
                        }
                    } catch (UnsupportedFlavorException | IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }), ctrlV, JComponent.WHEN_FOCUSED);

        final JMenuItem copy = new JMenuItem("Copy      CTRL+C");
        copy.addActionListener(e -> {
            String selected = Tab.this.editor.getSelectedText();

            if(selected==null)
                return;
            StringSelection clipString = new StringSelection(selected);
            ClipboardUtils.getClipboard().setContents(clipString,clipString);
        });

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
        HTMLEditorKit kit = (HTMLEditorKit) getEditor().getEditorKit();
        HTMLDocument doc = (HTMLDocument) getEditor().getStyledDocument();

        try {
            kit.insertHTML(doc, offset, html, 0, 0, null);
        } catch (BadLocationException | IOException e) {
            e.printStackTrace();
        }
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

    public void insertImage(int offset, Image image){
        URL url = ImageUtils.putImage(image);
        insertHtml(offset, "<img src='"+url+"'></img>");
    }
}