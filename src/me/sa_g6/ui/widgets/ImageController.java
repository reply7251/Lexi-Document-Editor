package me.sa_g6.ui.widgets;


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
import java.io.Serializable;

public class ImageController extends MouseAdapter implements MouseMotionListener, Serializable {
    Element curElem = null;
    boolean curElemImage = false;

    private int curOffset;

    int cursor;
    private Dimension startSize = null;

    private final transient Position.Bias[] bias = new Position.Bias[1];

    ImageResizer resizer;
    JEditorPane editor;

    public void setEditor(JEditorPane editor) {
        this.editor = editor;
    }

    public void updateResizer(){
        EnhancedHTMLDocument doc = (EnhancedHTMLDocument) editor.getDocument();
        HTMLDocument.RunElement attributes = (HTMLDocument.RunElement) resizer.elem.getAttributes();
        doc.hackWriteLock();
        attributes.addAttribute("id","resizing");
        doc.hackWriteUnlock();
        editor.setText(editor.getText().replaceAll("\n","</br>"));

        if(resizer != null){
            resizer.elem = doc.getElement("resizing");
        }
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

            resizer.repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        startSize = null;
        if(resizer != null){
            HTMLDocument.RunElement attributes = (HTMLDocument.RunElement) resizer.elem.getAttributes();
            EnhancedHTMLDocument doc = (EnhancedHTMLDocument) editor.getDocument();
            doc.hackWriteLock();
            attributes.removeAttribute("id");
            doc.hackWriteUnlock();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (resizer != null && startSize != null) {
            int x = resizer.getX();
            int y = resizer.getY();
            boolean shift = (e.getModifiersEx() & InputEvent.SHIFT_DOWN_MASK) != 0;
            /*
            int dx = e.getX() - startPos.x;
            int dy = e.getY() - startPos.y;
            */
            int width = e.getX();
            int height = e.getY();

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


/*

                case Cursor.N_RESIZE_CURSOR -> {
                    if (!(h - dy < 50)) {
                        resizer.setBounds(x, y + dy, w, h - dy);
                    }
                }
                case Cursor.W_RESIZE_CURSOR -> {
                    if (!(w - dx < 50)) {
                        resizer.setBounds(x + dx, y, w - dx, h);
                    }
                }
                case Cursor.NW_RESIZE_CURSOR -> {
                    if (!(w - dx < 50) && !(h - dy < 50)) {
                        resizer.setBounds(x + dx, y + dy, w - dx, h - dy);
                    }
                }
                case Cursor.NE_RESIZE_CURSOR -> {
                    if (!(w + dx < 50) && !(h - dy < 50)) {
                        resizer.setBounds(x, y + dy, w + dx, h - dy);
                        startPos = new Point(e.getX(), startPos.y);
                    }
                }
                case Cursor.SW_RESIZE_CURSOR -> {
                    if (!(w - dx < 50) && !(h + dy < 50)) {
                        resizer.setBounds(x + dx, y, w - dx, h + dy);
                        startPos = new Point(startPos.x, e.getY());
                    }
                }
 */