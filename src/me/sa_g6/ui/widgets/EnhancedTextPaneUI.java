package me.sa_g6.ui.widgets;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTextPaneUI;

public class EnhancedTextPaneUI extends BasicTextPaneUI {
    public void hackModelChanged(){
        modelChanged();
    }
}
