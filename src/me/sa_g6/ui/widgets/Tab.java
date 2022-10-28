package me.sa_g6.ui.widgets;

import me.sa_g6.iterator.ElementTreeIterator;
import me.sa_g6.formatting.FullDisplayMode;
import me.sa_g6.ui.MainWindow;
import me.sa_g6.utils.BetterAction;
import me.sa_g6.utils.ImageCache;
import me.sa_g6.utils.Size;
import me.sa_g6.utils.StringUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.Element;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class Tab extends JPanel {

    EnhancedTextPane editor = new EnhancedTextPane();
    JPopupMenu popup = new JPopupMenu();
    public boolean puretext = false;
    public final BetterAction.UndoManager undo;

    JTextArea debug1;
    JTextPane debug2;

    public Tab(){
        this(false);
    }

    public Tab(boolean debug){
        super();

        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        MouseEventBus eventBus = new MouseEventBus();
/*
        MouseObererverable oberserver = new MouseObererverable();


        addMouseListener(eventBus);
        addMouseMotionListener(eventBus);
*/
        //editor.setContentType("text/html");
        EnhancedHTMLDocument enhancedHTMLDocument = new EnhancedHTMLDocument();
        EnhancedHTMLDocument.EnhancedHTMLEditorKit enhancedHTMLEditorKit = new EnhancedHTMLDocument.EnhancedHTMLEditorKit();
        enhancedHTMLEditorKit.setEventBus(eventBus);
        editor.setDocument(enhancedHTMLDocument);
        editor.setEditorKit(enhancedHTMLEditorKit);
        ImageCache.getImageCache(editor.getDocument());
        insertHtml(0,"<html><body></body></html>");
        EnhancedHTMLDocument doc = (EnhancedHTMLDocument) editor.getDocument();
        JScrollPane pane = new JScrollPane(editor);

        if(debug){

            debug1 = new JTextArea();
            debug1.setEditable(false);
            JScrollPane debugPane1 = new JScrollPane(debug1);
            debugPane1.getVerticalScrollBar().setUnitIncrement(16);

            debug2 = new JTextPane();
            debug2.setEditable(false);
            JPanel noWrapPanel = new JPanel(new BorderLayout());
            noWrapPanel.add(debug2);
            JScrollPane debugPane2 = new JScrollPane(noWrapPanel);
            debugPane2.getVerticalScrollBar().setUnitIncrement(16);

            MutableAttributeSet lineSpacing = new SimpleAttributeSet();
            StyleConstants.setLineSpacing(lineSpacing, (float) -0.3);
            debug2.setParagraphAttributes(lineSpacing, false);

            registerDocumentListener();

            layout.setHorizontalGroup(
                    layout.createSequentialGroup()
                            .addComponent(pane).addGap(5)
                            .addGroup(
                                    layout.createParallelGroup()
                                            .addComponent(debugPane1,200,200,300)
                                            .addComponent(debugPane2,200,200,300)
                            )
            );
            layout.setVerticalGroup(
                    layout.createParallelGroup().addComponent(pane)
                            .addGroup(
                                    layout.createSequentialGroup()
                                            .addComponent(debugPane1,200,200,1000)
                                            .addGap(5)
                                            .addComponent(debugPane2,200,200,1000)
                            )
            );

        }else{
            layout.setHorizontalGroup(layout.createParallelGroup().addComponent(pane));
            layout.setVerticalGroup(layout.createParallelGroup().addComponent(pane));
        }
        KeyStroke ctrlV = KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK);
        editor.unregisterKeyboardAction(ctrlV);
        editor.registerKeyboardAction(new BetterAction.PasteAction(), ctrlV, JComponent.WHEN_FOCUSED);
        KeyStroke ctrlC = KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK);
        editor.unregisterKeyboardAction(ctrlC);
        editor.registerKeyboardAction(new BetterAction.CopyAction(), ctrlC, JComponent.WHEN_FOCUSED);


        final JMenuItem copy = new JMenuItem("Copy      CTRL+C");
        copy.addActionListener(new BetterAction.CopyAction());

        popup.add(copy);
        copy.setEnabled(true);
        editor.setComponentPopupMenu(popup);

        KeyStroke ctrlZ = KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK);
        KeyStroke ctrlY = KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK);
        undo = new BetterAction.UndoManager();
        doc.addUndoableEditListener(undo);
        editor.registerKeyboardAction(new BetterAction.UndoAction(undo),ctrlZ, JComponent.WHEN_FOCUSED);
        editor.registerKeyboardAction(new BetterAction.RedoAction(undo),ctrlY, JComponent.WHEN_FOCUSED);


        SimpleAttributeSet attr = new SimpleAttributeSet();
        StyleConstants.setSpaceAbove(attr,8);
        StyleConstants.setSpaceBelow(attr,8);
        editor.getStyledDocument().setParagraphAttributes(0,Integer.MAX_VALUE,attr,false);


        editor.registerKeyboardAction(e -> {
            puretext = !puretext;
            System.out.println(puretext);

            doc.hackFireChangedUpdate(doc.new DefaultDocumentEvent(0, doc.getLength(), DocumentEvent.EventType.CHANGE));
        },KeyStroke.getKeyStroke(KeyEvent.VK_G, KeyEvent.CTRL_DOWN_MASK), JComponent.WHEN_FOCUSED);
    }

    public void registerDocumentListener(){
        if(debug1 != null){
            editor.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    update();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    update();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    update();
                }

                void update(){
                    debug1.setText(editor.getText());
                    StringBuilder builder = new StringBuilder();
                    StringUtils.elementToString(builder, editor.getDocument().getDefaultRootElement());
                    debug2.setText(builder.toString());
                }
            });
        }
    }

    public EnhancedTextPane getEditor() {
        return editor;
    }

    public EnhancedHTMLDocument getDocument(){
        return (EnhancedHTMLDocument) editor.getDocument();
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