package me.sa_g6.ui.widgets;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import java.awt.*;

public class ImageResizer extends JComponent {
    Element elem;
    public ImageResizer(ImageController controller,Element element){
        elem = element;
        setLayout(new BorderLayout());
        addMouseListener(controller);
        addMouseMotionListener(controller);
        setBorder(new ResizableBorder(8));
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        HTMLDocument.RunElement attributes = (HTMLDocument.RunElement) elem.getAttributes();
        EnhancedHTMLDocument doc = (EnhancedHTMLDocument) elem.getDocument();
        doc.hackWriteLock();
        attributes.addAttribute(HTML.Attribute.WIDTH, String.valueOf(width));
        attributes.addAttribute(HTML.Attribute.HEIGHT, String.valueOf(height));
        doc.hackWriteUnlock();
    }
}
