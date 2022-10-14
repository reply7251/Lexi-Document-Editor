package me.sa_g6.ui.widgets;

import me.sa_g6.formatting.Alignment;
import me.sa_g6.ui.MainWindow;

import javax.swing.*;
import java.awt.event.ActionListener;

public class AlignmentMenuItem extends IMenuItem{
    public AlignmentMenuItem(String text, Alignment alignment) {
        super(text, (e)->{
                alignment.perform(getTab().getEditor());
        });
    }
}
