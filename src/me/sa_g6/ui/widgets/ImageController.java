package me.sa_g6.ui.widgets;

import me.sa_g6.utils.BetterAction;
import me.sa_g6.utils.Size;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.UndoableEditEvent;
import javax.swing.plaf.TextUI;
import javax.swing.text.*;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

public class ImageController implements Serializable, MouseObserver {

    private IMouseEventSubject action;
    Element curElem = null;
    boolean curElemImage = false;

    private int curOffset;

    int cursor;
    private Point startPos = null;
    private Dimension startSize = null;
    private AttributeSet startAttrs = null;

    private final transient Position.Bias[] bias = new Position.Bias[1];

    ImageResizer resizer;
    JTextPane editor;

    @Override
    public void update(MouseEvent e) {
        switch (e.getID()) {
            case MouseEvent.MOUSE_CLICKED:
                mouseClicked(e);
                break;
            case MouseEvent.MOUSE_PRESSED:
                mousePressed(e);
                break;
            case MouseEvent.MOUSE_RELEASED:
                mouseReleased();
                break;
            case MouseEvent.MOUSE_MOVED:
                mouseMoved(e);
                break;
            case MouseEvent.MOUSE_EXITED:
                mouseExited(e);
                break;
            case MouseEvent.MOUSE_DRAGGED:
                mouseDragged(e);
                break;

        }
    }

    public void setEditor(JTextPane editor) {
        this.editor = editor;
    }

    public void updateResizer() {
        EnhancedHTMLDocument doc = (EnhancedHTMLDocument) editor.getDocument();
        Element elem = resizer.elem;
        int start = elem.getStartOffset(), len = elem.getEndOffset() - start;
        doc.hackFireChangedUpdate(doc.new DefaultDocumentEvent(start, len, DocumentEvent.EventType.CHANGE));
    }

    public void removeResizer(){
        if (resizer != null) {
            editor.remove(resizer);
            resizer.invalidate();
            editor.revalidate();
            editor.repaint();
            resizer = null;
        }
    }


    public void mouseClicked(MouseEvent e) {
        removeResizer();
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
    }


    public void mouseMoved(MouseEvent e) {
        if (e.getSource() instanceof JEditorPane editor) {
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
        if (resizer != null && resizer.hasFocus()) {
            resizer.setCursor(Cursor.getPredefinedCursor(((ResizableBorder) resizer.getBorder()).getCursor(e)));
        }
    }


    public void mouseExited(MouseEvent e) {
        if (e.getSource() instanceof JComponent c) {
            c.setCursor(Cursor.getDefaultCursor());
        }
    }


    public void mousePressed(MouseEvent e) {
        if (resizer != null && resizer.hasFocus()) {
            ResizableBorder border = (ResizableBorder) resizer.getBorder();
            cursor = border.getCursor(e);
            startSize = resizer.getSize();
            startPos = e.getPoint();
            startAttrs = resizer.elem.getAttributes().copyAttributes();

            resizer.repaint();
        }
    }


    public void mouseReleased() {
        if (resizer != null) {
            HTMLDocument.RunElement attributes = (HTMLDocument.RunElement) resizer.elem.getAttributes();
            EnhancedHTMLDocument doc = (EnhancedHTMLDocument) editor.getDocument();
            doc.hackWriteLock();
            attributes.removeAttribute(HTML.Attribute.ID);
            doc.hackWriteUnlock();
            Element elem = resizer.elem;
            int start = elem.getStartOffset(), len = elem.getEndOffset() - start;

            AbstractDocument.DefaultDocumentEvent event = doc.new DefaultDocumentEvent(start, len, DocumentEvent.EventType.CHANGE);
            event.addEdit(new BetterAction.AttributesChangeEdit(elem, startAttrs));
            doc.fireUndoableEditUpdate(new UndoableEditEvent(this, event));

            event.end();

        }
        startSize = null;
        startPos = null;
    }


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
                        if (shift) {
                            width = (int) (height / 1.0 / startSize.height * startSize.width);
                        } else {
                            width = resizer.getWidth();
                        }
                        resizer.setBounds(new Size<>(width, height));
                    }
                }
                case Cursor.E_RESIZE_CURSOR -> {
                    if ((width > 20)) {
                        if (shift) {
                            height = (int) (width / 1.0 / startSize.width * startSize.height);
                        } else {
                            height = resizer.getHeight();
                        }
                        resizer.setBounds(new Size<>(width, height));
                    }
                }
                case Cursor.SE_RESIZE_CURSOR -> {
                    if ((width > 20) && (height > 20)) {
                        if (shift) {
                            if (width / 1.0 / startSize.width > height / 1.0 / startSize.height) {
                                height = (int) (width / 1.0 / startSize.width * startSize.height);
                            } else {
                                width = (int) (height / 1.0 / startSize.height * startSize.width);
                            }
                        }
                        resizer.setBounds(new Size<>(width, height));
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