package me.sa_g6.ui.view;

import me.sa_g6.ui.TableDocument;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.html.HTML;
import javax.swing.text.html.ImageView;
import javax.swing.text.html.StyleSheet;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.net.URL;

public class PatchedImageView extends ImageView {
    /**
     * Creates a new view that represents an IMG element.
     *
     * @param elem the element to create a view for
     */
    Rectangle fBounds = new Rectangle();
    int width, height;
    static final int DEFAULT_WIDTH = 38;

    Image image;
    ImageHandler ih = new ImageHandler();

    public PatchedImageView(Element elem) {
        super(elem);
    }

    @Override
    public URL getImageURL() {
        return null;
    }

    @Override
    protected StyleSheet getStyleSheet() {
        return ((TableDocument)getDocument()).getStyleSheet();
    }

    @Override
    public Image getImage() {
        Object v = getElement().getAttributes().getAttribute("img");
        if(v instanceof BufferedImage){
            BufferedImage bi = (BufferedImage) v;
            width = bi.getWidth();
            height = bi.getHeight();

            ImageIcon ii = new ImageIcon();
            ii.setImage(bi);
            return image = bi;
        }
        return null;
    }

    @Override
    public void paint(Graphics g, Shape a) {

        Rectangle rect = (a instanceof Rectangle) ? (Rectangle)a :
                a.getBounds();
        Rectangle clip = g.getClipBounds();

        fBounds.setBounds(rect);
        paintHighlights(g, a);
        //paintBorder(g, rect);
        if (clip != null) {
            g.clipRect(rect.x, rect.y,
                    rect.width,
                    rect.height);
        }

        Container host = getContainer();
        Image img = getImage();
        if (img != null) {
            if (! hasPixels(img)) {
                // No pixels yet, use the default
                Icon icon = getLoadingImageIcon();
                if (icon != null) {
                    icon.paintIcon(host, g,
                            rect.x , rect.y );
                }
            }
            else {
                // Draw the image
                g.drawImage(img, rect.x , rect.y ,
                        width, height, ih);
            }
        }
        else {
            Icon icon = getNoImageIcon();
            if (icon != null) {
                icon.paintIcon(host, g,
                        rect.x , rect.y );
            }
        }
        if (clip != null) {
            // Reset clip.
            g.setClip(clip.x, clip.y, clip.width, clip.height);
        }
    }


    private void repaint(long delay) {
        if (getParent() != null && fBounds != null) {
            getContainer().repaint(delay, fBounds.x, fBounds.y, fBounds.width,
                    fBounds.height);
        }
    }

    private boolean hasPixels(Image image) {
        return image != null &&
                (image.getHeight(ih) > 0) &&
                (image.getWidth(ih) > 0);
    }

    private void paintHighlights(Graphics g, Shape shape) {
        Container container = getContainer();
        if (container instanceof JTextComponent) {
            JTextComponent tc = (JTextComponent)container;
            Highlighter h = tc.getHighlighter();
            if (h instanceof LayeredHighlighter) {
                ((LayeredHighlighter)h).paintLayeredHighlights
                        (g, getStartOffset(), getEndOffset(), shape, tc, this);
            }
        }
    }

    private class ImageHandler implements ImageObserver {
        // This can come on any thread. If we are in the process of reloading
        // the image and determining our state (loading == true) we don't fire
        // preference changed, or repaint, we just reset the fWidth/fHeight as
        // necessary and return. This is ok as we know when loading finishes
        // it will pick up the new height/width, if necessary.
        public boolean imageUpdate(Image img, int flags, int x, int y,
                                   int newWidth, int newHeight ) {
            if (img != image || image == null || getParent() == null) {
                return false;
            }

            // Bail out if there was an error:
            if ((flags & (ABORT|ERROR)) != 0) {
                repaint(0);
                synchronized(PatchedImageView.this) {
                    if (image == img) {
                        // Be sure image hasn't changed since we don't
                        // initialy synchronize
                        image = null;
                    }
                }
                return false;
            }

            // Repaint when done or when new pixels arrive:
            return ((flags & ALLBITS) == 0);
        }
    }
}
