package me.sa_g6.ui.view;

import java.awt.*;
import javax.swing.text.*;

public class RowView extends BoxView {
    public RowView(Element elem) {
        super(elem, View.X_AXIS);
    }

    public float getPreferredSpan(int axis) {
        return super.getPreferredSpan(axis);
    }

    protected void layout(int width, int height) {
        super.layout(width, height);
    }

    public float getMinimumSpan(int axis) {
        return getPreferredSpan(axis);
    }

    public float getMaximumSpan(int axis) {
        return getPreferredSpan(axis);
    }

    public float getAlignment(int axis) {
        return 0;
    }

    protected void paintChild(Graphics g, Rectangle alloc, int index) {
        super.paintChild(g, alloc, index);
        g.setColor(Color.black);
        int h = (int) getPreferredSpan(View.Y_AXIS) - 1;
        g.drawLine(alloc.x, alloc.y, alloc.x, alloc.y + h);
        g.drawLine(alloc.x + alloc.width, alloc.y, alloc.x + alloc.width, alloc.y + h);
    }
}
