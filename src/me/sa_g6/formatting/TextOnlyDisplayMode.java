package me.sa_g6.formatting;

import me.sa_g6.formatting.DisplayMode;
import me.sa_g6.ui.MainWindow;
import me.sa_g6.ui.widgets.EnhancedHTMLDocument;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.event.KeyEvent;

public class TextOnlyDisplayMode implements DisplayMode {
    JTextPane curJTextPane;
    MainWindow mainWindow;

    @Override

    public void perform(JTextPane editor) {
        System.out.println("eee");
        curJTextPane=editor;
        SimpleAttributeSet attr = new SimpleAttributeSet();
        StyleConstants.setSpaceAbove(attr,1);
        StyleConstants.setSpaceBelow(attr,1);
        editor.getStyledDocument().setParagraphAttributes(0,Integer.MAX_VALUE,attr,false);
        MainWindow.getInstance().getCurrentTab().puretext=true;

    }

}
