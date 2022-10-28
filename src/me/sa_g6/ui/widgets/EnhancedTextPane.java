package me.sa_g6.ui.widgets;

import javax.swing.*;
import java.io.Serial;

public class EnhancedTextPane extends JTextPane{
    public void editElement(){
        if(ui instanceof EnhancedTextPaneUI eUI){
            eUI.hackModelChanged();
        }else{
            ui = new EnhancedTextPaneUI();
            ui.installUI(this);
            ((EnhancedTextPaneUI)ui).hackModelChanged();
        }
    }
}
