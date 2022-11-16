package me.sa_g6.ui.widgets;

import me.sa_g6.ui.MainWindow;

import javax.swing.*;
import java.awt.event.ActionListener;

public class IMenuItem extends JMenuItem {
    public IMenuItem(String text, ActionListener listener){
        super(text);

        addActionListener(listener);
        addActionListener((e)->{
            if(getTab().getEditor().getEditorKit() instanceof EnhancedHTMLDocument.EnhancedHTMLEditorKit kit){
                kit.imageController.removeResizer();
            }
        });
    }

    protected static final Tab getTab(){
        return (Tab) MainWindow.getInstance().getCurrentTab();
    }
}

