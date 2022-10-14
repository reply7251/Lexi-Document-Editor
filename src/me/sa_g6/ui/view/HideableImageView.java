package me.sa_g6.ui.view;

import me.sa_g6.ui.MainWindow;

import javax.swing.text.Element;
import javax.swing.text.html.ImageView;
import java.awt.*;

public class HideableImageView extends ImageView {
    /**
     * Creates a new view that represents an IMG element.
     *
     * @param elem the element to create a view for
     */
    public HideableImageView(Element elem) {
        super(elem);
    }

    @Override
    public void paint(Graphics g, Shape a) {
        if(!MainWindow.getInstance().getCurrentTab().puretext){
            super.paint(g,a);
        }
    }

    @Override
    public float getPreferredSpan(int axis) {
        if(!MainWindow.getInstance().getCurrentTab().puretext){
            return super.getPreferredSpan(axis);
        }
        return 0;
    }
}
