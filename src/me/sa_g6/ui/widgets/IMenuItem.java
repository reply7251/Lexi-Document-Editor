package me.sa_g6.ui.widgets;

import javax.swing.*;
import java.awt.event.ActionListener;

public abstract class IMenuItem extends JMenuItem {
    public IMenuItem(String text, ActionListener listener){
        super(text);

        addActionListener(listener);
    }
}
