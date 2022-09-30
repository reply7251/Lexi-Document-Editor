package me.sa_g6.ui.widgets;


import me.sa_g6.utils.BetterAction;

import javax.swing.*;
import javax.swing.plaf.TextUI;
import javax.swing.text.*;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;

public class ImageController extends MouseAdapter implements MouseMotionListener, Serializable {
    Element curElem = null;
    boolean curElemImage = false;

    private int curOffset;

    int cursor;
    private Point startPos = null;
    private Dimension startSize = null;

    private final transient Position.Bias[] bias = new Position.Bias[1];

    ImageResizer resizer;
    JTextPane editor;

    public void setEditor(JTextPane editor) {
        this.editor = editor;
    }

    public void updateResizer(){
        EnhancedHTMLDocument doc = (EnhancedHTMLDocument) editor.getDocument();
        HTMLDocument.RunElement attributes = (HTMLDocument.RunElement) resizer.elem.getAttributes();
        doc.hackWriteLock();
        attributes.removeAttribute(HTML.Attribute.ID);
        attributes.addAttribute(HTML.Attribute.ID,"resizing");
        doc.hackWriteUnlock();
        Element elem = resizer.elem;
        int start = elem.getStartOffset(), len = elem.getEndOffset() - start;
        StringWriter writer = new StringWriter();
        try {
            editor.getEditorKit().write(writer, elem.getDocument(),start, len);
            String img = writer.toString();
            img = img.substring(20,img.length()-19).trim();
            doc.remove(start,len);
            boolean mayHasNewLine = BetterAction.mayHasNewLine(editor,start);
            BetterAction.insertHtml(editor,start,img, mayHasNewLine ? null : HTML.Tag.IMG);
            AbstractDocument.BranchElement parent = (AbstractDocument.BranchElement) doc.getElement("resizing").getParentElement();
            int index = parent.getElementIndex(start);
            if(parent.getElementCount() > index+1){
                Object o = parent.getElement(index+1).getAttributes().getAttribute("CR");
                if(o != null && (boolean) o){
                    parent.replace(index+1,1,new Element[0]);
                }
            }
            /*
            if(mayHasNewLine){

                if(doc.getText(elem.getEndOffset(),1).equals("\n")){
                    doc.remove(elem.getEndOffset(),1);
                }
            }

             */
        } catch (IOException | BadLocationException ex) {
        }

        resizer.elem = doc.getElement("resizing");
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() instanceof JEditorPane editor && editor.isEnabled()) {
            if (curElemImage) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    int width = Integer.parseInt((String) curElem.getAttributes().getAttribute(HTML.Attribute.WIDTH));
                    int height = Integer.parseInt((String) curElem.getAttributes().getAttribute(HTML.Attribute.HEIGHT));
                    resizer = new ImageResizer(this, curElem);
                    try {
                        Rectangle2D rect = editor.modelToView2D(curOffset);
                        resizer.setBounds((int) rect.getX(), (int) rect.getY(), width, height);
                    } catch (BadLocationException ex) {
                        ex.printStackTrace();
                    }
                    editor.add(resizer);
                    resizer.repaint();
                    resizer.requestFocus();
                    return;
                }
            }
        }
        if(resizer != null){
            editor.remove(resizer);
            resizer.invalidate();
            editor.repaint();
            resizer = null;
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(e.getSource() instanceof JEditorPane editor){
            if (!editor.isEnabled()) {
                return;
            }

            Point pt = new Point(e.getX(), e.getY());
            int pos = editor.getUI().viewToModel2D(editor, pt, bias);
            if (bias[0] == Position.Bias.Backward && pos > 0) {
                pos--;
            }
            if (pos >= 0 && (editor.getDocument() instanceof HTMLDocument doc)) {
                curOffset = pos;
                Element elem = doc.getCharacterElement(pos);
                if (!doesElementContainLocation(editor, elem, pos, e.getX(), e.getY())) {
                    elem = null;
                }
                if (curElem != elem || curElemImage) {
                    curElem = elem;
                    curElemImage = false;
                    if (elem != null) {
                        AttributeSet a = elem.getAttributes();
                        if (a.getAttribute(HTML.Tag.A) == null) {
                            curElemImage = (a.getAttribute(StyleConstants.NameAttribute) == HTML.Tag.IMG);
                        }
                    }
                }
            }
        }
        if(resizer != null && resizer.hasFocus()){
            resizer.setCursor(Cursor.getPredefinedCursor(((ResizableBorder)resizer.getBorder()).getCursor(e)));
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if(e.getSource() instanceof JComponent c){
            c.setCursor(Cursor.getDefaultCursor());
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(resizer != null && resizer.hasFocus()){
            ResizableBorder border = (ResizableBorder)resizer.getBorder();
            cursor = border.getCursor(e);
            startSize = resizer.getSize();
            startPos = e.getPoint();

            resizer.repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        startSize = null;
        startPos = null;
        if(resizer != null){
            HTMLDocument.RunElement attributes = (HTMLDocument.RunElement) resizer.elem.getAttributes();
            EnhancedHTMLDocument doc = (EnhancedHTMLDocument) editor.getDocument();
            doc.hackWriteLock();
            attributes.removeAttribute(HTML.Attribute.ID);
            doc.hackWriteUnlock();
            doc.finishEdit();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (resizer != null && startSize != null) {
            int x = resizer.getX();
            int y = resizer.getY();
            boolean shift = (e.getModifiersEx() & InputEvent.SHIFT_DOWN_MASK) != 0;

            int dx = e.getX() - startPos.x;
            int dy = e.getY() - startPos.y;

            int width = startSize.width + dx;
            int height = startSize.height + dy;

            switch (cursor) {
                case Cursor.S_RESIZE_CURSOR -> {
                    if ((height > 20)) {
                        if (shift){
                            width = (int) (height / 1.0 / startSize.height * startSize.width);
                        }else {
                            width = resizer.getWidth();
                        }
                        resizer.setBounds(x, y, width, height);
                    }
                }
                case Cursor.E_RESIZE_CURSOR -> {
                    if ((width > 20)) {
                        if (shift){
                            height = (int) (width / 1.0 / startSize.width * startSize.height);
                        }else {
                            height = resizer.getHeight();
                        }
                        resizer.setBounds(x, y, width, height);
                    }
                }
                case Cursor.SE_RESIZE_CURSOR -> {
                    if ((width > 20) && (height > 20)) {
                        if(shift){
                            if(width / 1.0 /startSize.width > height / 1.0 / startSize.height){
                                height = (int) (width / 1.0 / startSize.width * startSize.height);
                            }else {
                                width = (int) (height / 1.0 / startSize.height * startSize.width);
                            }
                        }
                        resizer.setBounds(x, y, width, height);
                    }
                }
                default -> {
                    return;
                }
            }
            updateResizer();
            resizer.setCursor(Cursor.getPredefinedCursor(cursor));
        }
    }

    private boolean doesElementContainLocation(JEditorPane editor, Element e, int offset, int x, int y) {
        if (e != null && offset > 0 && e.getStartOffset() == offset) {
            try {
                TextUI ui = editor.getUI();
                Shape s1 = ui.modelToView2D(editor, offset, Position.Bias.Forward);
                if (s1 == null) {
                    return false;
                }
                Rectangle r1 = (s1 instanceof Rectangle) ? (Rectangle) s1 : s1.getBounds();
                Shape s2 = ui.modelToView2D(editor, e.getEndOffset(), Position.Bias.Backward);
                if (s2 != null) {
                    Rectangle r2 = (s2 instanceof Rectangle) ? (Rectangle) s2 : s2.getBounds();
                    r1.add(r2);
                }
                return r1.contains(x, y);
            } catch (BadLocationException ignored) {
            }
        }
        return true;
    }
}