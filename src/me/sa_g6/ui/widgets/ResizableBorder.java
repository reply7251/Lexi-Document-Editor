package me.sa_g6.ui.widgets;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseEvent;

public class ResizableBorder implements Border {
    static int[] locations = {
            SwingConstants.SOUTH, SwingConstants.EAST, SwingConstants.SOUTH_EAST
    };
    /*
            SwingConstants.NORTH, SwingConstants.SOUTH, SwingConstants.WEST,
            SwingConstants.EAST, SwingConstants.NORTH_WEST,
            SwingConstants.NORTH_EAST, SwingConstants.SOUTH_WEST,
            SwingConstants.SOUTH_EAST
    */

    static int[] cursors = {
            Cursor.S_RESIZE_CURSOR, Cursor.E_RESIZE_CURSOR, Cursor.SE_RESIZE_CURSOR
    };
    /*
            Cursor.N_RESIZE_CURSOR, Cursor.S_RESIZE_CURSOR, Cursor.W_RESIZE_CURSOR,
            Cursor.E_RESIZE_CURSOR, Cursor.NW_RESIZE_CURSOR, Cursor.NE_RESIZE_CURSOR,
            Cursor.SW_RESIZE_CURSOR, Cursor.SE_RESIZE_CURSOR
     */

    int margin;

    public ResizableBorder(int margin){
        this.margin = margin;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        g.setColor(Color.black);
        g.drawRect(x + margin / 2, y + margin / 2, width - margin, height - margin);
        for (int location : locations) {
            Rectangle rect = getRectangle(x, y, width, height, location);
            if (rect != null) {

                g.setColor(Color.WHITE);
                g.fillRect(rect.x, rect.y, rect.width - 1, rect.height - 1);
                g.setColor(Color.BLACK);
                g.drawRect(rect.x, rect.y, rect.width - 1, rect.height - 1);
            }
        }
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(margin,margin,margin,margin);
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }

    private Rectangle getRectangle(int x, int y, int w, int h, int location) {
        return switch (location) {
            case SwingConstants.NORTH -> new Rectangle(x + w / 2 - margin / 2, y, margin, margin);
            case SwingConstants.SOUTH -> new Rectangle(x + w / 2 - margin / 2, y + h - margin, margin, margin);
            case SwingConstants.WEST -> new Rectangle(x, y + h / 2 - margin / 2, margin, margin);
            case SwingConstants.EAST -> new Rectangle(x + w - margin, y + h / 2 - margin / 2, margin, margin);
            case SwingConstants.NORTH_WEST -> new Rectangle(x, y, margin, margin);
            case SwingConstants.NORTH_EAST -> new Rectangle(x + w - margin, y, margin, margin);
            case SwingConstants.SOUTH_WEST -> new Rectangle(x, y + h - margin, margin, margin);
            case SwingConstants.SOUTH_EAST -> new Rectangle(x + w - margin, y + h - margin, margin, margin);
            default -> new Rectangle();
        };
    }

    public int getCursor(MouseEvent me) {

        var c = me.getComponent();
        int w = c.getWidth();
        int h = c.getHeight();

        for (int i = 0; i < locations.length; i++) {

            var rect = getRectangle(0, 0, w, h, locations[i]);
            if (rect != null && rect.contains(me.getPoint())) {
                return cursors[i];
            }
        }

        return Cursor.getDefaultCursor().getType();
    }
}
