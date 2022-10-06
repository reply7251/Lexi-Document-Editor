package me.sa_g6.ui.widgets;

import me.sa_g6.utils.ClipboardUtils;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.undo.CompoundEdit;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.StringWriter;

public class ImageResizer extends JComponent {
    Element elem;
    ImageController controller;
    public ImageResizer(ImageController controller,Element element){
        this.controller = controller;
        elem = element;
        setLayout(new BorderLayout());
        addMouseListener(controller);
        addMouseMotionListener(controller);
        setBorder(new ResizableBorder(8));
        registerKeyboardAction(e->{
            StringWriter writer = new StringWriter();
            int start = elem.getStartOffset(), len = elem.getEndOffset() - start;
            try {
                controller.editor.getEditorKit().write(writer, elem.getDocument(),start, len);
                String img = writer.toString();
                img = img.substring(20,img.length()-19).trim();
                ClipboardUtils.getClipboard().setContents(new ClipboardUtils.HTMLTransferable(img), null);
            } catch (IOException | BadLocationException ex) {
                ex.printStackTrace();
            }
        },KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK),JComponent.WHEN_FOCUSED);
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        HTMLDocument.RunElement attrs = (HTMLDocument.RunElement) elem.getAttributes();
        if(!attrs.getAttribute(HTML.Attribute.WIDTH).equals(String.valueOf(width))
                || !attrs.getAttribute(HTML.Attribute.HEIGHT).equals(String.valueOf(height))){
            EnhancedHTMLDocument doc = (EnhancedHTMLDocument) elem.getDocument();
            doc.startEdit();
            doc.hackWriteLock();
            attrs.addAttribute(HTML.Attribute.WIDTH, String.valueOf(width));
            attrs.addAttribute(HTML.Attribute.HEIGHT, String.valueOf(height));
            doc.hackWriteUnlock();
        }
    }
}
