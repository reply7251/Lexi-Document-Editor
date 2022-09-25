package me.sa_g6.ui.widgets;

import javax.swing.*;
import java.awt.event.ActionListener;

public class IColorMenuItem extends JMenuItem {
    public IColorMenuItem(String text, ActionListener listener) {
        super(text);
        addActionListener(listener);
    }
}
